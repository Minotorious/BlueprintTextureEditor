package BlueprintTextureEditor;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.text.DocumentFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.AttributeSet;
import javax.swing.text.AbstractDocument;
import java.awt.Toolkit;
import java.awt.Desktop;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.nio.ByteBuffer;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class BlueprintTextureEditorGUI extends javax.swing.JFrame {

    public BlueprintTextureEditorGUI() {
        initComponents();
    }


    public static byte[] decompress(String input) throws IOException{
        try(ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPInputStream gzip = new GZIPInputStream(new FileInputStream(input))){
            //create a temp buffer/array to read the data from the stream
            byte[] tmpBuffer = new byte[1024];
            //once the end of stream has been reached, the read() method returns -1,
            //so while the number of read bytes is >= 0, we continue to read to our
            //temp buffer/array
            int n;
            while((n = gzip.read(tmpBuffer)) >= 0){
                //write the content of the temp buffer/array to the output stream
                out.write(tmpBuffer, 0, n);
            }
            //store the content of the output stream in a new byte array
            return out.toByteArray();
        }
    }
 
    public static byte[] compress(byte[] content) throws IOException{
        try(ByteArrayOutputStream out = new ByteArrayOutputStream(); GZIPOutputStream gzip = new GZIPOutputStream(out);){
            gzip.write(content);
            gzip.close();
        return out.toByteArray();
        }
    }
    
    public class BlueprintObject{
      public short typeid;
      public byte variation;
      public float posx;
      public float posy;
      public float posz;
      public short rotx;
      public short roty;
      public short rotz;
    }
    
    public class BlueprintConstruction{
        public short typeid;
        public int texture;
        public float posx;
        public float posy;
        public float posz;
        public short rotx;
        public short roty;
        public short rotz;
        public short sizex;
        public short sizey;
        public short sizez;
        public byte repetitionsh;
        public byte repetitionsv;
        public byte gap;
    }
    
    public class BlockTextures{
        public byte typeid;
        public byte currentTexture;
        public byte newTexture;
    }
    
    public class ConstructionTextures{
        public short typeid;
        public int currentTexture;
        public int newTexture;
    }
    
    public class ObjectTextures{
        public short typeid;
        public byte currentVariation;
        public byte newVariation;
    }
    
    public static class Texturedata{
        private static ArrayList<BlockTextures> BlockTex;
        private static ArrayList<ConstructionTextures> BeamTex;
        private static ArrayList<ConstructionTextures> PlankTex;
        private static ArrayList<ConstructionTextures> LogTex;
        private static ArrayList<ConstructionTextures> TriangleTex;
        private static ArrayList<ConstructionTextures> WindowTex;
        private static ArrayList<ObjectTextures> ObjTex;
        private static ArrayList<ConstructionTextures> AllConstrTex;
        
        public static ArrayList<BlockTextures> getBlockTex(){
            return BlockTex;
        }
        
        public static ArrayList<ConstructionTextures> getBeamTex(){
            return BeamTex;
        }
        
        public static ArrayList<ConstructionTextures> getPlankTex(){
            return PlankTex;
        }
        
        public static ArrayList<ConstructionTextures> getLogTex(){
            return LogTex;
        }
        
        public static ArrayList<ConstructionTextures> getTriangleTex(){
            return TriangleTex;
        }
        
        public static ArrayList<ConstructionTextures> getWindowTex(){
            return WindowTex;
        }
        
        public static ArrayList<ObjectTextures> getObjTex(){
            return ObjTex;
        }
        
        public static ArrayList<ConstructionTextures> getAllConstrTex(){
            return AllConstrTex;
        }
        
        public static void setBlockTex(ArrayList<BlockTextures> BT){
            BlockTex = BT;
        }
        
        public static void setBeamTex(ArrayList<ConstructionTextures> CT){
            BeamTex = CT;
        }
        
        public static void setPlankTex(ArrayList<ConstructionTextures> CT){
            PlankTex = CT;
        }
        
        public static void setLogTex(ArrayList<ConstructionTextures> CT){
            LogTex = CT;
        }
        
        public static void setTriangleTex(ArrayList<ConstructionTextures> CT){
            TriangleTex = CT;
        }
        
        public static void setWindowTex(ArrayList<ConstructionTextures> CT){
            WindowTex = CT;
        }
        
        public static void setObjTex(ArrayList<ObjectTextures> OT){
            ObjTex = OT;
        }
        
        public static void setAllConstrTex(ArrayList<ConstructionTextures> CT){
            AllConstrTex = CT;
        }
    }
    
    public static class databank{
        private static ArrayList<BlueprintConstruction> ConstrData;
        private static ArrayList<BlueprintObject> ObjectData;
        private static short[] BlockData;
        private static byte version;
        private static short sizex;
        private static short sizey;
        private static short sizez;
        private static byte[] ImageData;
        
        public static ArrayList<BlueprintConstruction> getConstr(){
            return ConstrData;
        }
        
        public static ArrayList<BlueprintObject> getObj(){
            return ObjectData;
        }
        
        public static short[] getBlock(){
            return BlockData;
        }
        
        public static byte getVersion(){
            return version;
        }
        
        public static short getSizex(){
            return sizex;
        }
        
        public static short getSizey(){
            return sizey;
        }
        
        public static short getSizez(){
            return sizez;
        }
        
        public static byte[] getImage(){
            return ImageData;
        }
        
        public static void setConstr(ArrayList<BlueprintConstruction> C){
            ConstrData = C;
        }
        
        public static void setObj(ArrayList<BlueprintObject> O){
            ObjectData = O;
        }
        
        public static void setBlock(short[] B){
            BlockData = B;
        }
        
        public static void setVersion(byte V){
            version = V;
        }
        
        public static void setSizex(short Sx){
            sizex = Sx;
        }
        
        public static void setSizey(short Sy){
            sizey = Sy;
        }
        
        public static void setSizez(short Sz){
            sizez = Sz;
        }
        
        public static void setImage(byte[] I){
            ImageData = I;
        }
    }
    
    public class SizeFilter extends DocumentFilter {

        private int maxCharacters;

        public SizeFilter(int maxChars) {
            maxCharacters = maxChars;
        }

        @Override
        public void insertString(FilterBypass fb, int offs, String str, AttributeSet a) throws BadLocationException {
            if ((fb.getDocument().getLength() + str.length()) <= maxCharacters)
                super.insertString(fb, offs, str, a);
            else
                Toolkit.getDefaultToolkit().beep();
        }

        @Override
        public void replace(FilterBypass fb, int offs, int length, String str, AttributeSet a) throws BadLocationException {
            if ((fb.getDocument().getLength() + str.length() - length) <= maxCharacters)
                super.replace(fb, offs, length, str, a);
            else
                Toolkit.getDefaultToolkit().beep();
        }
    }
    
    public class SizeIntFilter extends DocumentFilter {
        
        private int maxCharacters;

        public SizeIntFilter(int maxChars) {
            maxCharacters = maxChars;
        }
        
        @Override
        public void insertString(DocumentFilter.FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            StringBuffer buffer = new StringBuffer(string);
            for (int i = buffer.length() - 1; i >= 0; i--) {
                char ch = buffer.charAt(i);
                if (!Character.isDigit(ch)) {
                    buffer.deleteCharAt(i);
                }
            }
            if ((fb.getDocument().getLength() + string.length()) <= maxCharacters)
                super.insertString(fb, offset, buffer.toString(), attr);
            else
                Toolkit.getDefaultToolkit().beep();
        }

        @Override
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
            if (length > 0) fb.remove(offset, length);
            insertString(fb, offset, string, attr);
        }
    }
    
    public static class ObjectInfo{
        public int typeid;
        public String name;
    }
    
    public static class ObjectList{
        public static ArrayList<ObjectInfo> Objects;
        
        public static void setObjectList(ArrayList<ObjectInfo> OL){
            Objects = OL;
        }
        
        public static ArrayList<ObjectInfo> getObjectList(){
            return Objects;
        }
    }
    
    public static class LocaleInfo{
        public String field;
        public String text;
    }
    
    public static class LocaleList{
        public static ArrayList<LocaleInfo> englishLocale;
        public static ArrayList<LocaleInfo> germanLocale;
        
        public static void setEnglishLocale(ArrayList<LocaleInfo> EN){
            englishLocale = EN;
        }
        
        public static ArrayList<LocaleInfo> getEnglishLocale(){
            return englishLocale;
        }
        
        public static void setGermanLocale(ArrayList<LocaleInfo> DE){
            germanLocale = DE;
        }
        
        public static ArrayList<LocaleInfo> getGermanLocale(){
            return germanLocale;
        }
    }
    
    public String LocalebyName(ArrayList<LocaleInfo> locale, String input){
        String output = "";
        
        for(int i=0;i<locale.size();i++){
            if(locale.get(i).field.equals(input)){
                output = locale.get(i).text;
                break;
            }
        }
        
        return output;
    }
    
    String locale = "en";
    
    private void ApplyLocale(){
        if(locale.equals("en")){
            ArrayList<LocaleInfo> localelines = LocaleList.getEnglishLocale();
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(LocalebyName(localelines, "jPanel1")));
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(LocalebyName(localelines, "jPanel2")));
            jMenu1.setText(LocalebyName(localelines, "jMenu1"));
            jMenu2.setText(LocalebyName(localelines, "jMenu2"));
            jMenu3.setText(LocalebyName(localelines, "jMenu3"));
            jMenuItem1.setText(LocalebyName(localelines, "jMenuItem1"));
            jMenuItem2.setText(LocalebyName(localelines, "jMenuItem2"));
            jMenuItem3.setText(LocalebyName(localelines, "jMenuItem3"));
            jMenuItem4.setText(LocalebyName(localelines, "jMenuItem4"));
            jLabel1.setText(LocalebyName(localelines, "jLabel1"));
            jLabel2.setText(LocalebyName(localelines, "jLabel2"));
            jLabel3.setText(LocalebyName(localelines, "jLabel3"));
            jLabel4.setText(LocalebyName(localelines, "jLabel4"));
            jLabel5.setText(LocalebyName(localelines, "jLabel5"));
            jLabel6.setText(LocalebyName(localelines, "jLabel6"));
            jLabel7.setText(LocalebyName(localelines, "jLabel7"));
            jLabel8.setText(LocalebyName(localelines, "jLabel8"));
            jLabel9.setText(LocalebyName(localelines, "jLabel9"));
            jButton1.setText(LocalebyName(localelines, "jButton1"));
            jButton2.setText(LocalebyName(localelines, "jButton2"));
            jButton3.setText(LocalebyName(localelines, "jButton3"));
            jButton4.setText(LocalebyName(localelines, "jButton4"));
            jButton5.setText(LocalebyName(localelines, "jButton5"));
            jButton6.setText(LocalebyName(localelines, "jButton6"));
            jButton7.setText(LocalebyName(localelines, "jButton7"));
            jButton8.setText(LocalebyName(localelines, "jButton8"));
            jButton9.setText(LocalebyName(localelines, "jButton9"));
            jButton10.setText(LocalebyName(localelines, "jButton10"));
        }
        else if(locale.equals("de")){
            ArrayList<LocaleInfo> localelines = LocaleList.getGermanLocale();
            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(LocalebyName(localelines, "jPanel1")));
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(LocalebyName(localelines, "jPanel2")));
            jMenu1.setText(LocalebyName(localelines, "jMenu1"));
            jMenu2.setText(LocalebyName(localelines, "jMenu2"));
            jMenu3.setText(LocalebyName(localelines, "jMenu3"));
            jMenuItem1.setText(LocalebyName(localelines, "jMenuItem1"));
            jMenuItem2.setText(LocalebyName(localelines, "jMenuItem2"));
            jMenuItem3.setText(LocalebyName(localelines, "jMenuItem3"));
            jMenuItem4.setText(LocalebyName(localelines, "jMenuItem4"));
            jLabel1.setText(LocalebyName(localelines, "jLabel1"));
            jLabel2.setText(LocalebyName(localelines, "jLabel2"));
            jLabel3.setText(LocalebyName(localelines, "jLabel3"));
            jLabel4.setText(LocalebyName(localelines, "jLabel4"));
            jLabel5.setText(LocalebyName(localelines, "jLabel5"));
            jLabel6.setText(LocalebyName(localelines, "jLabel6"));
            jLabel7.setText(LocalebyName(localelines, "jLabel7"));
            jLabel8.setText(LocalebyName(localelines, "jLabel8"));
            jLabel9.setText(LocalebyName(localelines, "jLabel9"));
            jButton1.setText(LocalebyName(localelines, "jButton1"));
            jButton2.setText(LocalebyName(localelines, "jButton2"));
            jButton3.setText(LocalebyName(localelines, "jButton3"));
            jButton4.setText(LocalebyName(localelines, "jButton4"));
            jButton5.setText(LocalebyName(localelines, "jButton5"));
            jButton6.setText(LocalebyName(localelines, "jButton6"));
            jButton7.setText(LocalebyName(localelines, "jButton7"));
            jButton8.setText(LocalebyName(localelines, "jButton8"));
            jButton9.setText(LocalebyName(localelines, "jButton9"));
            jButton10.setText(LocalebyName(localelines, "jButton10"));
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser1 = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        ((AbstractDocument)jTextField1.getDocument()).setDocumentFilter(
            new SizeFilter(24));
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        ((AbstractDocument)jTextField6.getDocument()).setDocumentFilter(
            new SizeIntFilter(3));
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Blueprint Texture Editor");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Textures"));
        jPanel1.setPreferredSize(new java.awt.Dimension(200, 200));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Texture Changing Interface"));

        jTextField1.setName(""); // NOI18N

        jLabel1.setText("Blueprint Name:");

        jLabel2.setText("Blueprint Creator:");

        jTextField2.setEditable(false);

        jLabel3.setText("Blueprint World:");

        jTextField3.setEditable(false);

        jButton1.setText("Edit Blocks");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Edit Planks");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Edit Beams");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Edit Logs");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Edit All Construction");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel4.setText("Editing:");

        jTextField4.setEditable(false);

        jButton6.setText("Edit Objects");
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Previous");
        jButton7.setEnabled(false);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Next");
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel5.setText("Current Texture:");

        jTextField5.setEditable(false);

        jLabel6.setText("New Texture:");

        jButton9.setText("Edit Triangles");
        jButton9.setEnabled(false);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton10.setText("Edit Windows");
        jButton10.setEnabled(false);
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jLabel7.setText("Element Name:");

        jTextField7.setEditable(false);

        jTextField8.setEditable(false);

        jTextField9.setEditable(false);

        jLabel8.setText("Element:");

        jLabel9.setText("of");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton10))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField7))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel6)
                                .addGap(11, 11, 11)
                                .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(3, 3, 3)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton6)
                    .addComponent(jButton9)
                    .addComponent(jButton10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(208, 208, 208))
        );

        jMenuBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Open...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Save...");
        jMenuItem2.setEnabled(false);
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Save Image...");
        jMenuItem4.setEnabled(false);
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Locale");

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("English");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Deutsch");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuBar1.add(jMenu3);

        jMenu2.setText("Help");

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem3.setText("Documentation");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        javax.swing.JComponent parent = null;
        int returnVal = jFileChooser1.showOpenDialog(parent);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            //Static Variable Cleanup
            ArrayList<BlueprintConstruction> BCI = new ArrayList<>();
            databank.setConstr(BCI);
            ArrayList<BlueprintObject> BOI = new ArrayList<>();
            databank.setObj(BOI);
            short[] BBI = new short[0];
            databank.setBlock(BBI);
            ArrayList<ConstructionTextures> BeTI = new ArrayList<>();
            Texturedata.setBeamTex(BeTI);
            ArrayList<ConstructionTextures> PTI = new ArrayList<>();
            Texturedata.setPlankTex(PTI);
            ArrayList<ConstructionTextures> LTI = new ArrayList<>();
            Texturedata.setLogTex(LTI);
            ArrayList<ConstructionTextures> WTI = new ArrayList<>();
            Texturedata.setWindowTex(WTI);
            ArrayList<ConstructionTextures> TTI = new ArrayList<>();
            Texturedata.setTriangleTex(TTI);
            ArrayList<BlockTextures> BlTI = new ArrayList<>();
            Texturedata.setBlockTex(BlTI);
            ArrayList<ObjectTextures> OTI = new ArrayList<>();
            Texturedata.setObjTex(OTI);
            ArrayList<ConstructionTextures> ACT = new ArrayList<>();
            Texturedata.setAllConstrTex(ACT);
            
            //Opening and Processing Blueprint File
            File file = jFileChooser1.getSelectedFile();
            try {
                byte[] BPdata = decompress(file.getPath());
                ByteBuffer BPbuffer = ByteBuffer.wrap(BPdata);
                byte BPversion = BPbuffer.get();
                databank.setVersion(BPversion);
                short BPsizex = BPbuffer.getShort();
                databank.setSizex(BPsizex);
                short BPsizey = BPbuffer.getShort();
                databank.setSizey(BPsizey);
                short BPsizez = BPbuffer.getShort();
                databank.setSizez(BPsizez);
                long BPtimestamp = BPbuffer.getLong();
                String BPhexstrtimestamp = Long.toHexString(BPtimestamp);
                Long BPlongtimestamp = Long.valueOf(BPhexstrtimestamp, 16);
                Date BPhumantimestamp = new Date(BPlongtimestamp);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String BPhumanstrtimestamp = sdf.format(BPhumantimestamp);
                int BPnamel = BPbuffer.getInt();
                byte[] BPname = new byte[BPnamel];
                BPbuffer.get(BPname);
                String BPnamestr = new String(BPname, "UTF-8");
                jTextField1.setText(BPnamestr);
                int BPauthorl = BPbuffer.getInt();
                byte[] BPauthor = new byte[BPauthorl];
                BPbuffer.get(BPauthor);
                String BPauthorstr = new String(BPauthor, "UTF-8");
                jTextField2.setText(BPauthorstr);
                int BPworldl = BPbuffer.getInt();
                byte[] BPworld = new byte[BPworldl];
                BPbuffer.get(BPworld);
                String BPworldstr = new String(BPworld, "UTF-8");
                jTextField3.setText(BPworldstr);
                int BPblockl = BPbuffer.getInt();
                if (BPblockl <= 0){
                    jButton1.setEnabled(false);
                }
                else{
                    jButton1.setEnabled(false);
                    short[] BPblock = new short[BPblockl/2];
                    for (int i=0;i<BPblockl/2;i++){
                        BPblock[i] = BPbuffer.getShort();
                    }
                    databank.setBlock(BPblock);
                }

                int BPobjectl = BPbuffer.getInt();
                if (BPobjectl <= 0){
                    jButton6.setEnabled(false);
                }
                else{
                    jButton6.setEnabled(true);
                    int BPobjectN = BPbuffer.getInt();
                    ArrayList<BlueprintObject> BPobjects = new ArrayList<>(BPobjectN);
                    for(int i=0;i<BPobjectN;i++){
                        BlueprintObject BPobject = new BlueprintObject();
                        BPobject.typeid = BPbuffer.getShort();
                        BPobject.variation = BPbuffer.get();
                        BPobject.posx = BPbuffer.getFloat();
                        BPobject.posy = BPbuffer.getFloat();
                        BPobject.posz = BPbuffer.getFloat();
                        BPobject.rotx = BPbuffer.getShort();
                        BPobject.roty = BPbuffer.getShort();
                        BPobject.rotz = BPbuffer.getShort();
                        BPobjects.add(BPobject);
                    }
                    databank.setObj(BPobjects);
                    ArrayList<ObjectTextures> ObjTex = new ArrayList<>();
                    for (int i=0;i<BPobjectN;i++){
                        ObjectTextures OT = new ObjectTextures();
                        OT.typeid = BPobjects.get(i).typeid;
                        OT.currentVariation = BPobjects.get(i).variation;
                        OT.newVariation = BPobjects.get(i).variation;
                        ObjTex.add(OT);
                    }
                    Texturedata.setObjTex(ObjTex);
                }

                int BPconstrl = BPbuffer.getInt();
                if (BPconstrl <= 0){
                    jButton2.setEnabled(false);
                    jButton3.setEnabled(false);
                    jButton4.setEnabled(false);
                    jButton5.setEnabled(false);
                    jButton9.setEnabled(false);
                    jButton10.setEnabled(false);
                }
                else{
                    int BPconstrN = BPbuffer.getInt();
                    ArrayList<BlueprintConstruction> BPconstrs = new ArrayList<>(BPconstrN);
                    for(int i=0;i<BPconstrN;i++){
                        BlueprintConstruction BPconstr = new BlueprintConstruction();
                        BPconstr.typeid = BPbuffer.getShort();
                        BPconstr.texture = BPbuffer.getInt();
                        BPconstr.posx = BPbuffer.getFloat();
                        BPconstr.posy = BPbuffer.getFloat();
                        BPconstr.posz = BPbuffer.getFloat();
                        BPconstr.rotx = BPbuffer.getShort();
                        BPconstr.roty = BPbuffer.getShort();
                        BPconstr.rotz = BPbuffer.getShort();
                        BPconstr.sizex = BPbuffer.getShort();
                        BPconstr.sizey = BPbuffer.getShort();
                        BPconstr.sizez = BPbuffer.getShort();
                        BPconstr.repetitionsh = BPbuffer.get();
                        BPconstr.repetitionsv = BPbuffer.get();
                        BPconstr.gap = BPbuffer.get();
                        BPconstrs.add(BPconstr);
                    }
                    databank.setConstr(BPconstrs);
                    ArrayList<ConstructionTextures> BeamTextures = new ArrayList<>();
                    ArrayList<ConstructionTextures> PlankTextures = new ArrayList<>();
                    ArrayList<ConstructionTextures> LogTextures = new ArrayList<>();
                    ArrayList<ConstructionTextures> WindowTextures = new ArrayList<>();
                    ArrayList<ConstructionTextures> TriangleTextures = new ArrayList<>();
                    ArrayList<ConstructionTextures> AllConstructionTextures = new ArrayList<>();

                    for (int i=0;i<197;i++){
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 3 && BPconstrs.get(j).texture == i){
                                ConstructionTextures BT = new ConstructionTextures();
                                BT.typeid = BPconstrs.get(j).typeid;
                                BT.currentTexture =  i;
                                BT.newTexture = i;
                                BeamTextures.add(BT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 2 && BPconstrs.get(j).texture == i){
                                ConstructionTextures PT = new ConstructionTextures();
                                PT.typeid = BPconstrs.get(j).typeid;
                                PT.currentTexture =  i;
                                PT.newTexture = i;
                                PlankTextures.add(PT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 10 && BPconstrs.get(j).texture == i){
                                ConstructionTextures LT = new ConstructionTextures();
                                LT.typeid = BPconstrs.get(j).typeid;
                                LT.currentTexture =  i;
                                LT.newTexture = i;
                                LogTextures.add(LT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 9 && BPconstrs.get(j).texture == i){
                                ConstructionTextures TT = new ConstructionTextures();
                                TT.typeid = BPconstrs.get(j).typeid;
                                TT.currentTexture =  i;
                                TT.newTexture = i;
                                TriangleTextures.add(TT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 5 && BPconstrs.get(j).texture == i){
                                ConstructionTextures WT = new ConstructionTextures();
                                WT.typeid = BPconstrs.get(j).typeid;
                                WT.currentTexture =  i;
                                WT.newTexture = i;
                                WindowTextures.add(WT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 6 && BPconstrs.get(j).texture == i){
                                ConstructionTextures WT = new ConstructionTextures();
                                WT.typeid = BPconstrs.get(j).typeid;
                                WT.currentTexture =  i;
                                WT.newTexture = i;
                                WindowTextures.add(WT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 7 && BPconstrs.get(j).texture == i){
                                ConstructionTextures WT = new ConstructionTextures();
                                WT.typeid = BPconstrs.get(j).typeid;
                                WT.currentTexture =  i;
                                WT.newTexture = i;
                                WindowTextures.add(WT);
                                break;
                            }
                        }
                        for (int j=0;j<BPconstrN;j++){
                            if (BPconstrs.get(j).typeid == 8 && BPconstrs.get(j).texture == i){
                                ConstructionTextures WT = new ConstructionTextures();
                                WT.typeid = BPconstrs.get(j).typeid;
                                WT.currentTexture =  i;
                                WT.newTexture = i;
                                WindowTextures.add(WT);
                                break;
                            }
                        }
                    }
                    Texturedata.setBeamTex(BeamTextures);
                    Texturedata.setPlankTex(PlankTextures);
                    Texturedata.setLogTex(LogTextures);
                    Texturedata.setTriangleTex(TriangleTextures);
                    Texturedata.setWindowTex(WindowTextures);

                    AllConstructionTextures.addAll(PlankTextures);
                    AllConstructionTextures.addAll(BeamTextures);
                    AllConstructionTextures.addAll(LogTextures);
                    AllConstructionTextures.addAll(TriangleTextures);
                    AllConstructionTextures.addAll(WindowTextures);
                    Texturedata.setAllConstrTex(AllConstructionTextures);

                    if (!BeamTextures.isEmpty()){
                        jButton3.setEnabled(true);
                    }
                    if (!PlankTextures.isEmpty()){
                        jButton2.setEnabled(true);
                    }
                    if (!LogTextures.isEmpty()){
                        jButton4.setEnabled(true);
                    }
                    if (!TriangleTextures.isEmpty()){
                        jButton9.setEnabled(true);
                    }
                    if (!WindowTextures.isEmpty()){
                        jButton10.setEnabled(true);
                    }
                    if (jButton2.isEnabled() || jButton3.isEnabled() || jButton4.isEnabled() || jButton9.isEnabled() || jButton10.isEnabled()){
                        jButton5.setEnabled(true);
                    }
                }

                int BPimagel = BPbuffer.getInt();
                byte[] BPimage = new byte[BPimagel];
                BPbuffer.get(BPimage);
                databank.setImage(BPimage);
                jMenuItem2.setEnabled(true);
                jMenuItem4.setEnabled(true);
                jButton7.setEnabled(true);
                jButton8.setEnabled(true);
                jTextField4.setText("");
                jTextField5.setText("");
                jTextField6.setText("");
                jTextField7.setText("");
                jTextField8.setText("");
                jTextField9.setText("");
            }
            catch (IOException e){
            }
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        for (int j=0;j<databank.getConstr().size();j++){
            switch (databank.getConstr().get(j).typeid) {
                case 2:
                    for (int i=0;i<Texturedata.getPlankTex().size();i++){
                        if (databank.getConstr().get(j).texture == Texturedata.getPlankTex().get(i).currentTexture){
                            databank.getConstr().get(j).texture = Texturedata.getPlankTex().get(i).newTexture;
                        }
                    }
                    break;
                case 3:
                    for (int i=0;i<Texturedata.getBeamTex().size();i++){
                        if (databank.getConstr().get(j).texture == Texturedata.getBeamTex().get(i).currentTexture){
                            databank.getConstr().get(j).texture = Texturedata.getBeamTex().get(i).newTexture;
                        }
                    }
                    break;
                case 10:
                    for (int i=0;i<Texturedata.getLogTex().size();i++){
                        if (databank.getConstr().get(j).texture == Texturedata.getLogTex().get(i).currentTexture){
                            databank.getConstr().get(j).texture = Texturedata.getLogTex().get(i).newTexture;
                        }
                    }
                    break;
                case 9:
                    for (int i=0;i<Texturedata.getTriangleTex().size();i++){
                        if (databank.getConstr().get(j).texture == Texturedata.getTriangleTex().get(i).currentTexture){
                            databank.getConstr().get(j).texture = Texturedata.getTriangleTex().get(i).newTexture;
                        }
                    }
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    for (int i=0;i<Texturedata.getWindowTex().size();i++){
                        if (databank.getConstr().get(j).typeid == Texturedata.getWindowTex().get(i).typeid && databank.getConstr().get(j).texture == Texturedata.getWindowTex().get(i).currentTexture){
                            databank.getConstr().get(j).texture = Texturedata.getWindowTex().get(i).newTexture;
                        }
                    }
                    break;
                default:
                    break;
            }
        }
        for (int j=0;j<databank.getObj().size();j++){
            for (int i=0;i<Texturedata.getObjTex().size();i++){
                if (databank.getObj().get(j).typeid == Texturedata.getObjTex().get(i).typeid && databank.getObj().get(j).variation == Texturedata.getObjTex().get(i).currentVariation){
                    databank.getObj().get(j).variation = Texturedata.getObjTex().get(i).newVariation;
                }
            }
        }
        
        javax.swing.JComponent parent = null;
        jFileChooser1.setSelectedFile(new File(jTextField1.getText() + ".blueprint"));
        int returnVal = jFileChooser1.showSaveDialog(parent);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            try(ByteArrayOutputStream BPbytedata =  new ByteArrayOutputStream(); DataOutputStream BPoutdata = new DataOutputStream(BPbytedata); FileOutputStream fileout = new FileOutputStream(file)){
                
                BPoutdata.writeByte(databank.getVersion());
                BPoutdata.writeShort(databank.getSizex());
                BPoutdata.writeShort(databank.getSizey());
                BPoutdata.writeShort(databank.getSizez());
                Date date = new Date();
                long millisec = date.getTime();
                BPoutdata.writeLong(millisec);
                BPoutdata.writeInt(jTextField1.getText().length());
                BPoutdata.writeBytes(jTextField1.getText());
                BPoutdata.writeInt(jTextField2.getText().length());
                BPoutdata.writeBytes(jTextField2.getText());
                BPoutdata.writeInt(jTextField3.getText().length());
                BPoutdata.writeBytes(jTextField3.getText());
                if (databank.getBlock().length == 0){
                    BPoutdata.writeInt(-1);
                }
                else{
                    BPoutdata.writeInt(databank.getBlock().length*2);
                    for(int i=0;i<databank.getBlock().length;i++){
                        BPoutdata.writeShort(databank.getBlock()[i]);
                    }
                }
                if (databank.getObj().isEmpty()){
                    BPoutdata.writeInt(-1);
                }
                else{
                    BPoutdata.writeInt(databank.getObj().size()*21+4);
                    BPoutdata.writeInt(databank.getObj().size());
                    for(int i=0;i<databank.getObj().size();i++){
                        BPoutdata.writeShort(databank.getObj().get(i).typeid);
                        BPoutdata.writeByte(databank.getObj().get(i).variation);
                        BPoutdata.writeFloat(databank.getObj().get(i).posx);
                        BPoutdata.writeFloat(databank.getObj().get(i).posy);
                        BPoutdata.writeFloat(databank.getObj().get(i).posz);
                        BPoutdata.writeShort(databank.getObj().get(i).rotx);
                        BPoutdata.writeShort(databank.getObj().get(i).roty);
                        BPoutdata.writeShort(databank.getObj().get(i).rotz);
                    }
                }
                if (databank.getConstr().isEmpty()){
                    BPoutdata.writeInt(-1);
                }
                else{
                    BPoutdata.writeInt(databank.getConstr().size()*33+4);
                    BPoutdata.writeInt(databank.getConstr().size());
                    for(int i=0;i<databank.getConstr().size();i++){
                        BPoutdata.writeShort(databank.getConstr().get(i).typeid);
                        BPoutdata.writeInt(databank.getConstr().get(i).texture);
                        BPoutdata.writeFloat(databank.getConstr().get(i).posx);
                        BPoutdata.writeFloat(databank.getConstr().get(i).posy);
                        BPoutdata.writeFloat(databank.getConstr().get(i).posz);
                        BPoutdata.writeShort(databank.getConstr().get(i).rotx);
                        BPoutdata.writeShort(databank.getConstr().get(i).roty);
                        BPoutdata.writeShort(databank.getConstr().get(i).rotz);
                        BPoutdata.writeShort(databank.getConstr().get(i).sizex);
                        BPoutdata.writeShort(databank.getConstr().get(i).sizey);
                        BPoutdata.writeShort(databank.getConstr().get(i).sizez);
                        BPoutdata.writeByte(databank.getConstr().get(i).repetitionsh);
                        BPoutdata.writeByte(databank.getConstr().get(i).repetitionsv);
                        BPoutdata.writeByte(databank.getConstr().get(i).gap);
                    }
                }
                BPoutdata.writeInt(databank.getImage().length);
                BPoutdata.write(databank.getImage());
                byte[] byteout = BPbytedata.toByteArray();
                byte[] compressedbyteout = compress(byteout);

                fileout.write(compressedbyteout);
            }
            catch (IOException e){
            }
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
    public int cyclecount;
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextField4.setText("Blocks");
        jTextField7.setText("");
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getBlockTex().size()));
        cyclecount = 0;
        //BlockTextures Datapoint = Texturedata.getBlockTex().get(cyclecount);
        //jTextField5.setText(Integer.toString(Datapoint.currentTexture));
        //jTextField6.setText(Integer.toString(Datapoint.newTexture));
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        jTextField4.setText("Planks");
        jTextField7.setText("");
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getPlankTex().size()));
        cyclecount = 0;
        ConstructionTextures Datapoint = Texturedata.getPlankTex().get(cyclecount);
        jTextField5.setText(Integer.toString(Datapoint.currentTexture+21));
        jTextField6.setText(Integer.toString(Datapoint.newTexture+21));
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        jTextField4.setText("Beams");
        jTextField7.setText("");
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getBeamTex().size()));
        cyclecount = 0;
        ConstructionTextures Datapoint = Texturedata.getBeamTex().get(cyclecount);
        jTextField5.setText(Integer.toString(Datapoint.currentTexture+21));
        jTextField6.setText(Integer.toString(Datapoint.newTexture+21));
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        jTextField4.setText("Logs");
        jTextField7.setText("");
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getLogTex().size()));
        cyclecount = 0;
        ConstructionTextures Datapoint = Texturedata.getLogTex().get(cyclecount);
        jTextField5.setText(Integer.toString(Datapoint.currentTexture+21));
        jTextField6.setText(Integer.toString(Datapoint.newTexture+21));
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jTextField4.setText("All Construction");
        cyclecount = 0;
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getAllConstrTex().size()));
        ConstructionTextures Datapoint = Texturedata.getAllConstrTex().get(cyclecount);
        jTextField5.setText(Integer.toString(Datapoint.currentTexture+21));
        jTextField6.setText(Integer.toString(Datapoint.newTexture+21));
        switch (Datapoint.typeid) {
            case 2:
                jTextField7.setText("Plank");
                break;
            case 3:
                jTextField7.setText("Beam");
                break;
            case 10:
                jTextField7.setText("Log");
                break;
            case 9:
                jTextField7.setText("Triangle");
                break;
            case 5:
                jTextField7.setText("Window A");
                break;
            case 6:
                jTextField7.setText("Window B");
                break;
            case 7:
                jTextField7.setText("Window C");
                break;
            case 8:
                jTextField7.setText("Window D");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        jTextField4.setText("Objects");
        cyclecount = 0;
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getObjTex().size()));
        ObjectTextures Datapoint = Texturedata.getObjTex().get(cyclecount);
        jTextField5.setText(Byte.toString(Datapoint.currentVariation));
        jTextField6.setText(Byte.toString(Datapoint.newVariation));
        jTextField7.setText(ObjectList.getObjectList().get(Datapoint.typeid-1).name);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        jTextField4.setText("Windows");
        jTextField7.setText("");
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getWindowTex().size()));
        cyclecount = 0;
        ConstructionTextures Datapoint = Texturedata.getWindowTex().get(cyclecount);
        jTextField5.setText(Integer.toString(Datapoint.currentTexture+21));
        jTextField6.setText(Integer.toString(Datapoint.newTexture+21));
        switch (Datapoint.typeid) {
            case 5:
                jTextField7.setText("Window A");
                break;
            case 6:
                jTextField7.setText("Window B");
                break;
            case 7:
                jTextField7.setText("Window C");
                break;
            case 8:
                jTextField7.setText("Window D");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        jTextField4.setText("Triangles");
        jTextField7.setText("");
        jTextField8.setText(Integer.toString(1));
        jTextField9.setText(Integer.toString(Texturedata.getTriangleTex().size()));
        cyclecount = 0;
        ConstructionTextures Datapoint = Texturedata.getTriangleTex().get(cyclecount);
        jTextField5.setText(Integer.toString(Datapoint.currentTexture+21));
        jTextField6.setText(Integer.toString(Datapoint.newTexture+21));
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //Previous Button
        switch (jTextField4.getText()) {
            case "Planks":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getPlankTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getPlankTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount, OldDatapoint);
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getPlankTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getPlankTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Beams":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getBeamTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getBeamTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size(), OldDatapoint);
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getBeamTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getBeamTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Logs":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getLogTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getLogTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size()+Texturedata.getBeamTex().size(), OldDatapoint);
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getLogTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getLogTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Windows":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getWindowTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getWindowTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size()+Texturedata.getBeamTex().size()+Texturedata.getLogTex().size()+Texturedata.getTriangleTex().size(), OldDatapoint);
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getWindowTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getWindowTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    switch (NewDatapoint.typeid) {
                        case 5:
                            jTextField7.setText("Window A");
                            break;
                        case 6:
                            jTextField7.setText("Window B");
                            break;
                        case 7:
                            jTextField7.setText("Window C");
                            break;
                        case 8:
                            jTextField7.setText("Window D");
                            break;
                        default:
                            break;
                    }
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Triangles":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getTriangleTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getTriangleTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size()+Texturedata.getBeamTex().size()+Texturedata.getLogTex().size(), OldDatapoint);
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getTriangleTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getTriangleTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Objects":
                {
                    ObjectTextures OldDatapoint = new ObjectTextures();
                    OldDatapoint.typeid = Texturedata.getObjTex().get(cyclecount).typeid;
                    OldDatapoint.currentVariation = Byte.parseByte(jTextField5.getText());
                    OldDatapoint.newVariation = Byte.parseByte(jTextField6.getText());
                    Texturedata.getObjTex().set(cyclecount, OldDatapoint);
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getObjTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ObjectTextures NewDatapoint = Texturedata.getObjTex().get(cyclecount);
                    jTextField7.setText(ObjectList.getObjectList().get(NewDatapoint.typeid-1).name);
                    jTextField5.setText(Byte.toString(NewDatapoint.currentVariation));
                    jTextField6.setText(Byte.toString(NewDatapoint.newVariation));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
                case "All Construction":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getAllConstrTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getAllConstrTex().set(cyclecount, OldDatapoint);
                    switch (OldDatapoint.typeid) {
                        case 2:
                            Texturedata.getPlankTex().set(cyclecount, OldDatapoint);
                            break;
                        case 3:
                            Texturedata.getBeamTex().set(cyclecount-Texturedata.getPlankTex().size(), OldDatapoint);
                            break;
                        case 10:
                            Texturedata.getLogTex().set(cyclecount-Texturedata.getPlankTex().size()-Texturedata.getBeamTex().size(), OldDatapoint);
                            break;
                        case 9:
                            Texturedata.getTriangleTex().set(cyclecount-Texturedata.getPlankTex().size()-Texturedata.getBeamTex().size()-Texturedata.getLogTex().size(), OldDatapoint);
                            break;
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            Texturedata.getWindowTex().set(cyclecount-Texturedata.getPlankTex().size()-Texturedata.getBeamTex().size()-Texturedata.getLogTex().size()-Texturedata.getTriangleTex().size(), OldDatapoint);
                            break;
                        default:
                            break;
                    }
                    if (cyclecount == 0){
                        cyclecount = Texturedata.getAllConstrTex().size()-1;
                    }
                    else{
                        cyclecount -= 1;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getAllConstrTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    switch (NewDatapoint.typeid) {
                        case 2:
                            jTextField7.setText("Plank");
                            break;
                        case 3:
                            jTextField7.setText("Beam");
                            break;
                        case 10:
                            jTextField7.setText("Log");
                            break;
                        case 9:
                            jTextField7.setText("Triangle");
                            break;
                        case 5:
                            jTextField7.setText("Window A");
                            break;
                        case 6:
                            jTextField7.setText("Window B");
                            break;
                        case 7:
                            jTextField7.setText("Window C");
                            break;
                        case 8:
                            jTextField7.setText("Window D");
                            break;
                        default:
                            break;
                    }
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            default:
                break;
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        //Next Button
        switch (jTextField4.getText()) {
            case "Planks":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getPlankTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getPlankTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount, OldDatapoint);
                    if (cyclecount < Texturedata.getPlankTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getPlankTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Beams":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getBeamTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getBeamTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size(), OldDatapoint);
                    if (cyclecount < Texturedata.getBeamTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getBeamTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Logs":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getLogTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getLogTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size()+Texturedata.getBeamTex().size(), OldDatapoint);
                    if (cyclecount < Texturedata.getLogTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getLogTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Windows":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getWindowTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getWindowTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size()+Texturedata.getBeamTex().size()+Texturedata.getLogTex().size()+Texturedata.getTriangleTex().size(), OldDatapoint);
                    if (cyclecount < Texturedata.getWindowTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getWindowTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    switch (NewDatapoint.typeid) {
                        case 5:
                            jTextField7.setText("Window A");
                            break;
                        case 6:
                            jTextField7.setText("Window B");
                            break;
                        case 7:
                            jTextField7.setText("Window C");
                            break;
                        case 8:
                            jTextField7.setText("Window D");
                            break;
                        default:
                            break;
                    }
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Triangles":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getTriangleTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getTriangleTex().set(cyclecount, OldDatapoint);
                    Texturedata.getAllConstrTex().set(cyclecount+Texturedata.getPlankTex().size()+Texturedata.getBeamTex().size()+Texturedata.getLogTex().size(), OldDatapoint);
                    if (cyclecount < Texturedata.getTriangleTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }       
                    ConstructionTextures NewDatapoint = Texturedata.getTriangleTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "Objects":
                {
                    ObjectTextures OldDatapoint = new ObjectTextures();
                    OldDatapoint.typeid = Texturedata.getObjTex().get(cyclecount).typeid;
                    OldDatapoint.currentVariation = Byte.parseByte(jTextField5.getText());
                    OldDatapoint.newVariation = Byte.parseByte(jTextField6.getText());
                    Texturedata.getObjTex().set(cyclecount, OldDatapoint);
                    if (cyclecount < Texturedata.getObjTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }
                    ObjectTextures NewDatapoint = Texturedata.getObjTex().get(cyclecount);
                    jTextField7.setText(ObjectList.getObjectList().get(NewDatapoint.typeid-1).name);
                    jTextField5.setText(Byte.toString(NewDatapoint.currentVariation));
                    jTextField6.setText(Byte.toString(NewDatapoint.newVariation));
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            case "All Construction":
                {
                    ConstructionTextures OldDatapoint = new ConstructionTextures();
                    OldDatapoint.typeid = Texturedata.getAllConstrTex().get(cyclecount).typeid;
                    OldDatapoint.currentTexture = Integer.parseInt(jTextField5.getText())-21;
                    OldDatapoint.newTexture = Integer.parseInt(jTextField6.getText())-21;
                    Texturedata.getAllConstrTex().set(cyclecount, OldDatapoint);
                    switch (OldDatapoint.typeid) {
                        case 2:
                            Texturedata.getPlankTex().set(cyclecount, OldDatapoint);
                            break;
                        case 3:
                            Texturedata.getBeamTex().set(cyclecount-Texturedata.getPlankTex().size(), OldDatapoint);
                            break;
                        case 10:
                            Texturedata.getLogTex().set(cyclecount-Texturedata.getPlankTex().size()-Texturedata.getBeamTex().size(), OldDatapoint);
                            break;
                        case 9:
                            Texturedata.getTriangleTex().set(cyclecount-Texturedata.getPlankTex().size()-Texturedata.getBeamTex().size()-Texturedata.getLogTex().size(), OldDatapoint);
                            break;
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            Texturedata.getWindowTex().set(cyclecount-Texturedata.getPlankTex().size()-Texturedata.getBeamTex().size()-Texturedata.getLogTex().size()-Texturedata.getTriangleTex().size(), OldDatapoint);
                            break;
                        default:
                            break;
                    }
                    if (cyclecount < Texturedata.getAllConstrTex().size()-1){
                        cyclecount += 1;
                    }
                    else{
                        cyclecount = 0;
                    }
                    ConstructionTextures NewDatapoint = Texturedata.getAllConstrTex().get(cyclecount);
                    jTextField5.setText(Integer.toString(NewDatapoint.currentTexture+21));
                    jTextField6.setText(Integer.toString(NewDatapoint.newTexture+21));
                    switch (NewDatapoint.typeid){
                        case 2:
                            jTextField7.setText("Plank");
                            break;
                        case 3:
                            jTextField7.setText("Beam");
                            break;
                        case 10:
                            jTextField7.setText("Log");
                            break;
                        case 9:
                            jTextField7.setText("Triangle");
                            break;
                        case 5:
                            jTextField7.setText("Window A");
                            break;
                        case 6:
                            jTextField7.setText("Window B");
                            break;
                        case 7:
                            jTextField7.setText("Window C");
                            break;
                        case 8:
                            jTextField7.setText("Window D");
                            break;
                        default:
                            break;
                    }
                    jTextField8.setText(Integer.toString(cyclecount+1));
                    break;
                }
            default:
                break;
        }
    }//GEN-LAST:event_jButton8ActionPerformed

    public void DocOpen() throws IOException {
        String inputPdf = "/Documentation/Documentation.pdf";
        Path tempOutput = Files.createTempFile("Documentation", ".pdf");
        tempOutput.toFile().deleteOnExit();
        try (InputStream is = getClass().getResourceAsStream(inputPdf)) {
            Files.copy(is, tempOutput, StandardCopyOption.REPLACE_EXISTING);
        }
        Desktop.getDesktop().open(tempOutput.toFile());
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        try{
            DocOpen();
        }
        catch (IOException e){
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed
    
    public static BufferedImage verticalflip(BufferedImage img){
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }
    
    public static BufferedImage getImageFromArray(byte[] pixels, int width, int height) {
        int[] dst = new int[width * height];
        for (int i = 0, j = 0; i < dst.length; i++) {
            int a = pixels[j++] & 0xff;
            int b = pixels[j++] & 0xff;
            int g = pixels[j++] & 0xff;
            int r = pixels[j++] & 0xff;
            dst[i] = (a << 24) | (r << 16) | (g << 8) | b;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, dst, 0, width);

        image = verticalflip(image);

        return image;
    }
    
    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        byte[] imageinfo = databank.getImage();
        ByteBuffer imagebuffer = ByteBuffer.wrap(imageinfo);

        int Iwidth = imagebuffer.getInt();
        int Iheight = imagebuffer.getInt();
        byte formatl = imagebuffer.get();
        byte[] format = new byte[formatl];
        imagebuffer.get(format);
        //String formatstr = new String(format, "UTF-8");
        int Idatal = imagebuffer.getInt();
        byte[] imagedata = new byte[Idatal];
        imagebuffer.get(imagedata);
        byte mipmapdatal = imagebuffer.get();
        //byte[] mipmapdata = new byte[mipmapdatal];
        //imagebuffer.get(mipmapdata);
        
        BufferedImage image = getImageFromArray(imagedata,Iwidth,Iheight);
        
        javax.swing.JComponent parent = null;
        jFileChooser1.setSelectedFile(new File(jTextField1.getText() + ".png"));
        int returnVal = jFileChooser1.showSaveDialog(parent);
        if(returnVal == javax.swing.JFileChooser.APPROVE_OPTION) {
            File file = jFileChooser1.getSelectedFile();
            try{
                ImageIO.write(image, "png", file);
            }
            catch (IOException e){
            }
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        if (locale.equals("en")){
            
        }
        else{
            locale = "en";
            ApplyLocale();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        if (locale.equals("de")){
            
        }
        else{
            locale = "de";
            ApplyLocale();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed
    
    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    public static void main(String args[]) throws IOException{
        
        String ObjectTxt = "/ObjectData/RWObjects.txt";
        ArrayList<ObjectInfo> Objects =  new ArrayList<>();
        
        try (InputStream is = BlueprintTextureEditorGUI.class.getResourceAsStream(ObjectTxt);){
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] line = sCurrentLine.split("\\t");
                ObjectInfo Obj = new ObjectInfo();
                Obj.typeid = Integer.valueOf(line[0]);
                Obj.name = line[1];
                Objects.add(Obj);
            }
            ObjectList.setObjectList(Objects);
            br.close();
        }
        
        String EngLocTxt = "/Locales/english.txt";
        ArrayList<LocaleInfo> englishLocale =  new ArrayList<>();
        
        try (InputStream is = BlueprintTextureEditorGUI.class.getResourceAsStream(EngLocTxt);){
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] line = sCurrentLine.split("=");
                LocaleInfo Loc = new LocaleInfo();
                Loc.field = line[0];
                Loc.text = line[1];
                englishLocale.add(Loc);
            }
            LocaleList.setEnglishLocale(englishLocale);
            br.close();
        }
        
        String GerLocTxt = "/Locales/deutsch.txt";
        ArrayList<LocaleInfo> germanLocale =  new ArrayList<>();
        
        try (InputStream is = BlueprintTextureEditorGUI.class.getResourceAsStream(GerLocTxt);){
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                String[] line = sCurrentLine.split("=");
                LocaleInfo Loc = new LocaleInfo();
                Loc.field = line[0];
                Loc.text = line[1];
                germanLocale.add(Loc);
            }
            LocaleList.setGermanLocale(germanLocale);
            br.close();
        }
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BlueprintTextureEditorGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    // End of variables declaration//GEN-END:variables
}
