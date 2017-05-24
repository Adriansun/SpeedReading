package main;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class WriteText extends JPanel implements ActionListener 
{
	private static final long serialVersionUID = 1L;

    boolean running = false;

    File file;
    BufferedReader br;
    Image bakgrundsBild;
    Timer stoppTid;
    
    int lineCounter = 0;
    int wordIndex;
    int getIndex;
    int hastighet = 300;
    
    String activeText = "";
    String getSpeed;  
    
    String[] textArray = new String[0];
    ArrayList<String> backASentence = new ArrayList<String>();

    FileNameExtensionFilter filter;
    JFileChooser fc;
    JTextField textField;
    JTextField textSpeed;
    JButton bStart;
    JButton bPause;
    JButton bStepForward;
    JButton bStepBack;
    JButton bPickFile;
    JCheckBox repeat;
            
    public void initializeComponent()
    {
        JPanel p = new JPanel(new GridBagLayout());

        p.setOpaque(false);       

        bStart = new JButton("Start");
        bPause = new JButton("Pause");
        bStepForward = new JButton("Step forward");
        bStepBack = new JButton("          ^^          "); //This button does not do anything yet
        bPickFile = new JButton("Choose file");

        textField = new JTextField(activeText,10);
        textField.setEditable(false);
        textField.setBackground(Color.white);
        textField.setHorizontalAlignment(JLabel.CENTER);
        Font bigFont = textField.getFont().deriveFont(Font.PLAIN, 50f);
        textField.setFont(bigFont);
        textField.setBackground(Color.GRAY);
        
        textSpeed = new JTextField("" + hastighet,5);
        textSpeed.setEditable(true);
        textSpeed.setBackground(Color.white);
        textSpeed.setHorizontalAlignment(JLabel.CENTER);
        Font bigFonti = textSpeed.getFont().deriveFont(Font.BOLD, 15f);
        textSpeed.setFont(bigFonti);  
        
        repeat = new JCheckBox("Repeat");
        repeat.setSelected(false);
        repeat.setBackground(Color.gray);
                              
        bStart.addActionListener(this);
        bPause.addActionListener(this);
        bStepForward.addActionListener(this);
        bStepBack.addActionListener(this);
        bPickFile.addActionListener(this);
        textSpeed.addActionListener(this);
        
        GridBagConstraints c = new GridBagConstraints();
                
        c.insets = new Insets(10,10,10,10);
        
        c.gridx = 0;
        c.gridy = 0;
        p.add(textField,c);
        
        c.gridx = 0;
        c.gridy = 1;
        p.add(bStart, c);
        
        c.gridx = 0;
        c.gridy = 2;
        p.add(bPause, c);
        
        c.gridx = 0;
        c.gridy = 3;
        p.add(bStepForward, c);
        
        c.gridx = 0;
        c.gridy = 4;
        p.add(bStepBack, c);
        
        c.gridx = 0;
        c.gridy = 5;
        p.add(bPickFile, c);
        
        c.gridx = 0;
        c.gridy = 6;
        p.add(textSpeed, c);
        
        c.gridx = 0;
        c.gridy = 7;
        p.add(repeat, c);
        
        add(p);
    }
    
    public WriteText()
    {   
        this.stoppTid = new Timer(hastighet, this);
        bakgrundsBild = new ImageIcon(this.getClass().getResource("mof.jpg")).getImage();      
    }

    @Override
    public void paintComponent(Graphics g)
    {       
        super.paintComponent(g);    
        Graphics2D posBackground = (Graphics2D) g;
        int x = (this.getWidth() - bakgrundsBild.getWidth(null)) / 2;
        int y = (this.getHeight() - bakgrundsBild.getHeight(null)) / 2;
        posBackground.drawImage(bakgrundsBild, x, y, null);

        if (running)
            try 
            {
                textField.setText(getWord());
            } 
            catch (UnsupportedEncodingException ex) 
            {
                Logger.getLogger(WriteText.class.getName()).log(Level.SEVERE, null, ex);
            }      
    }  

    @Override
    public void actionPerformed(ActionEvent e)                  
    {  
        if (e.getSource() == bStart)
        {
            start();
        }
        else if (e.getSource() == bPause)
        {
            pause();
        }
        else if (e.getSource() == bStepForward)
        {
            try 
            {
                bStepForward();
            } 
            catch (UnsupportedEncodingException ex) 
            {
                Logger.getLogger(WriteText.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if (e.getSource() == bStepBack)
        {
            bStepBack();
        }
        else if (e.getSource() == bPickFile)
        {
            bPickFile();
        }
        else if (e.getSource() == textSpeed)
        {
            int updateSpeed = textSpeedMetoden();
            this.stoppTid.setDelay(updateSpeed);
        }
        
        repaint();                                              
    }   
    
    public int textSpeedMetoden()
    {   
        // Ord per sekund.
        getSpeed = textSpeed.getText(); 
        int speed = Integer.valueOf(getSpeed);
        if (speed < 60)
        {
            speed = 60; 
        }
        else if (speed > 1000)
        {
            speed = 1000;
        }
        
        double calcSpeed = (speed/60);
        int timerValue = (int) (1000/calcSpeed); 
        
        textSpeed.setText("" + speed);
        
        return timerValue;
    }
          
    public void start()
    {
        // Starta programmet.
        running = true;
        stoppTid.start();
    }   
  
    public void pause()
    {
        // Stoppa text flödet programmet.
        running = false;
        stoppTid.stop();
    }
    
    public void bStepForward() throws UnsupportedEncodingException
    {
        // Steppa forward
        running = false;
        textField.setText(getWord());
    }
    
    public void bPickFile()
    {
        // pick a file
        running = false;

        String userhome = System.getProperty("user.home");
        fc = new JFileChooser(userhome + "\\Desktop");
        filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION) 
        {
            try 
            {
                file = fc.getSelectedFile();              
                br = new BufferedReader(new FileReader(file));
            } 
            catch (FileNotFoundException ex) 
            {
                System.out.println("File does not exist");
            }
        }
    }
    
    public void bStepBack()
    {
        // steppa bakåt
        running = false;
    }
    
    private String getWord() throws UnsupportedEncodingException
    {
        if (wordIndex == textArray.length || wordIndex == textArray.length - 1)
        {
            stringToArray();
            wordIndex = 0;
            
            return textArray[0];
        }
        else
        {
            wordIndex +=1;
            getIndex = wordIndex;
            
            return textArray[wordIndex];
        }
    }
    
    public void stringToArray() throws UnsupportedEncodingException
    {
        String strLine = getLine();
        textArray = strLine.split("\\s"); 
    }
   
    private String getLine() throws UnsupportedEncodingException
    {
        String strLine;
        
        try 
        {   
            if ((strLine = br.readLine()) != null)
                {
                    //backASentence.add(0, strLine);
                    lineCounter ++; 
                    //System.out.println(lineCounter);
                    return (strLine);
                }    
        } 
        catch (IOException ex) 
        {
            return "Error when reading file.";
        }

        if(repeat.isSelected())
        {
            if (file != null)
            {
                try 
                {
                    br = new BufferedReader(new FileReader(file));
                    lineCounter = 0;
                } 
                catch (FileNotFoundException ex) 
                {
                    System.out.println("That doesnt work");
                }
            }
            else
            { 
                readFile();      
            }
        return "";
        }

        return "The file has ended.";
    }
    
    public void readFile() throws UnsupportedEncodingException
    {
        try
        {
            FileInputStream fstream = new FileInputStream(System.getProperty("user.dir") +  ("/defaultfile.txt"));
            DataInputStream in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            lineCounter = 0;
        }
        catch (FileNotFoundException e)
        {  
            System.err.println("Error: " + e.getMessage());
        }
    } 
}