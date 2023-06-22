package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


public class MainSence extends JPanel {
    private String phoneNumber;
    private ChromeDriver chromeDriver;
    private BufferedImage background;
    private TextField phoneText;
    private TextField text;
    private Window window;
    private JButton firstB;
    private JButton secondB;
    private String status="";
    private JLabel massageSent;
    public MainSence(Window window){
        this.window=window;
        this.setLayout(null);
        this.setBounds(0,0,window.WIDTH,window.HEIGHT);
        massgeSent();
        addBackgroundPicture();
        addFirstButton();
        Phone();
        textMassage();
        addSecondButton();
    }
    private void massgeSent(){
        massageSent= new JLabel("הודעה נשלחה!");
        massageSent.setBounds(window.WIDTH/3+window.WIDTH/15, window.HEIGHT/2+window.HEIGHT/10, window.WIDTH/4 , window.HEIGHT/20);
        massageSent.setForeground(Color.BLACK);
        massageSent.setFont(new Font("Serif",Font.BOLD,20));
        this.add(massageSent);
        massageSent.setVisible(false);
        repaint();
    }
    private void addBackgroundPicture() {
        try {
            this.background = ImageIO.read(new File("src/main/java/Images/background.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void openChrome()  {
        System.setProperty("webdriver.openqa.driver", "C:\\Users\\revit\\Downloads\\chromedriver_win32 (1)");
        this.chromeDriver = new ChromeDriver();
        this.chromeDriver.get("https://web.whatsapp.com/");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.chromeDriver.manage().window().maximize();
        WebElement searchBox;
        while (true) {
            try {
                searchBox = this.chromeDriver.findElement(By.xpath("//*[@id=\"side\"]/div[1]/div/div/div[2]/div/div[1]/p"));
                if (searchBox != null) {
                    secondB.setEnabled(true);
                    firstB.setEnabled(false);
                    break;
                }
            } catch (Exception ignored) {
            }
        }
    }
    private String lastMassage(){
        String lastMessage=null;
        List<WebElement> divElements;
        List<WebElement> list;

        WebElement lastDivElement;
        try {
            divElements= chromeDriver.findElements(By.cssSelector("div[role='row']"));
            lastDivElement=divElements.get(divElements.size()-1);
            list=lastDivElement.findElements(By.cssSelector("span[data-icon='msg-dblcheck']"));
            lastMessage=list.get(list.size()-1).getAccessibleName();

        }catch (Exception exception){
            return null;
        }
        return lastMessage;
    }
    private void openByPhone() throws InterruptedException{

        chromeDriver.get("https://web.whatsapp.com/send/?phone="+phoneNumber);
        WebElement send = null;
        while (send == null) {
            try {
                send = chromeDriver.findElement(By.xpath("//*[@id=\"main\"]/footer/div[1]/div/span[2]/div/div[2]/div[1]/div/div[1]/p"));
                send.sendKeys(text.getText());
                send.sendKeys(Keys.ENTER);
                massageSent.setVisible(true);
                repaint();
                new Thread(()->{
                    while (!status.equals("נקראה")){
                        try {
                            String lastMessageElement =lastMassage();
                            if (lastMessageElement==null){
                                massageSent.setText("הודעה נשלחה!");
                                repaint();
                            }else if(lastMessageElement.contains("נמסרה")||lastMessageElement.contains("Delivered")){
                                massageSent.setText("הודעה נמסרה!");
                                repaint();
                            }else if (lastMessageElement.contains("נקראה")||lastMessageElement.contains("Read")){
                                status="נקראה";
                                massageSent.setText("הודעה נקראה!");
                                repaint();
                                chromeDriver.close();
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();
            } catch (Exception ignored) {
            }
        }
    }
    public void Phone(){
        JLabel labelPhone = new JLabel("Phone       :");
        labelPhone.setBounds(window.WIDTH/3, window.HEIGHT/4, window.WIDTH/8 , window.HEIGHT/20);
        labelPhone.setForeground(Color.BLACK);
        labelPhone.setFont(new Font("Serif",Font.BOLD,20));
        this.add(labelPhone);

        this.phoneText = new TextField("");
        phoneText.setBounds(window.WIDTH/2, window.HEIGHT/4, window.WIDTH/8, window.HEIGHT/20);
        this.add(phoneText);
        phoneText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                    for (int i=0;i<phoneText.getText().length();i++) {
                        if (phoneText.getText().charAt(i) < '0' || phoneText.getText().charAt(i) > '9') {
                            System.out.println("you need to enter Letters");
                            phoneText.setText("");
                        }
                    }
                } catch (Exception exception) {
                    System.out.println("Please enter Letters");
                   phoneText.setText("");
                }
            }
        });
    }
    public void textMassage(){
        JLabel labelText = new JLabel("Text          :");
        labelText.setBounds(window.WIDTH/3, window.HEIGHT/3, window.WIDTH/8 ,window.HEIGHT/20);
        labelText.setForeground(Color.BLACK);
        labelText.setFont(new Font("Serif",Font.BOLD,20));
        this.add(labelText);

        this.text = new TextField("");
        text.setBounds(window.WIDTH/2, window.HEIGHT/3, window.WIDTH/8, window.HEIGHT/20);
        this.add(text);
        text.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                try {
                } catch (Exception exception) {
                    System.out.println("Please enter Letters");
                    phoneText.setText("");
                }
            }
        });
    }
    public  void  addFirstButton(){
        firstB=new JButton("Open Website");
        firstB.setBounds(window.WIDTH/3,window.HEIGHT/6,window.WIDTH/4+window.WIDTH/23,window.HEIGHT/20);
        this.add(firstB);
        firstB.addActionListener(e->{
            new Thread(this::openChrome).start();
        });
    }
    public  void  addSecondButton(){
        secondB=new JButton("Send Massage");
        secondB.setBounds(window.WIDTH/3,window.HEIGHT/2,window.WIDTH/4+window.WIDTH/23,window.HEIGHT/20);
        this.add(secondB);
        secondB.setEnabled(false);
        secondB.addActionListener(e->{
            massageSent.setText("הודעה נשלחה!");
            massageSent.setVisible(false);
            repaint();
            if (phoneText.getText().equals("")) {
                JOptionPane.showMessageDialog(null,"you need enter a phone number");
            } else  if (phoneText.getText().length() != 10||!phoneText.getText().startsWith("05")) {
                JOptionPane.showMessageDialog(null,"the phone number isn't good");
            } else  if (text.getText().equals("")){
                JOptionPane.showMessageDialog(null,"you massage is empty");
            }else {
                phoneNumber="972"+phoneText.getText().substring(1,10);
                new Thread(()->{
                    try {
                        openByPhone();
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                }).start();
                System.out.println(phoneNumber);
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        g.drawImage(this.background,0,0,getWidth(),getHeight(),this);
    }
}
