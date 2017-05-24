package main;

import java.io.UnsupportedEncodingException;
import javax.swing.*;                                               

public class SpeedReading 
{
    public static void main(String[] args) throws UnsupportedEncodingException 
    {
        JFrame theFrame = new JFrame("Adrians speed Reading software (AsRs) -  It's fantastic! (TM) | AsRs is a branch of the Iris systems.");
            
        theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
        theFrame.setSize(1030, 530);                                
        theFrame.setLocationRelativeTo(null);

        WriteText textia = new WriteText();                       
        theFrame.add(textia);
        textia.initializeComponent();
        textia.readFile();
        theFrame.setVisible(true);                                 
    }
}