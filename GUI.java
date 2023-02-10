import javax.swing.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileWriter;

public class GUI extends JFrame implements ActionListener
{
    private JButton saveButton;
    private JButton searchButton;
    private JTextField nameField;
    private JTextField contactField;
    private String fileName = "Contacts.txt";

    public GUI()
    {
        new JFrame();

        // JLabels
        JLabel nameLabel = new JLabel("Name: ");
        JLabel contactLabel = new JLabel("Contact Number: ");
        nameLabel.setBounds(25, 25, 145, 25);
        contactLabel.setBounds(25, 25, 145, 150);

        // JTextFields
        nameField = new JTextField(20);
        nameField.setBounds(145, 25, 200, 25);
        nameField.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                char c = e.getKeyChar();
                if (!(Character.isAlphabetic(c) || (c == KeyEvent.VK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_SHIFT) || (c == KeyEvent.VK_CONTROL) || (c == KeyEvent.VK_BACK_SPACE)))
                {
                    JOptionPane.showMessageDialog(null, "Name must not contain numbers or special characters.");
                    getToolkit().beep();
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
            }
        });
        contactField = new JTextField(20);
        contactField.setBounds(145, 90, 200, 25);
        contactField.addKeyListener(new KeyListener()
        {
            @Override
            public void keyTyped(KeyEvent e)
            {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || (c == KeyEvent.VK_SPACE) || (c == KeyEvent.VK_DELETE) || (c == KeyEvent.VK_BACK_SPACE)))
                {
                    JOptionPane.showMessageDialog(null, "Contact Number must be numerical value only.");
                    getToolkit().beep();
                    e.consume();
                }
            }

            @Override
            public void keyPressed(KeyEvent e)
            {
            }

            @Override
            public void keyReleased(KeyEvent e)
            {
            }
        });

        // JButtons
        saveButton = new JButton("Save");
        saveButton.setBounds(50, 150, 100, 30);
        saveButton.addActionListener(this);
        saveButton.setFocusable(false);
        searchButton = new JButton("Search");
        searchButton.setBounds(232, 150, 100, 30);
        searchButton.addActionListener(this);

        this.setTitle("Contact Book Program");
        ImageIcon logo = new ImageIcon("ContactBookIcon.png");
        this.setIconImage(logo.getImage());
        this.setLayout(null);
        this.setSize(400, 250);
        this.setLocationRelativeTo(null);
        this.add(nameLabel);
        this.add(contactLabel);
        this.add(nameField);
        this.add(contactField);
        this.add(saveButton);
        this.add(searchButton);
        this.setResizable(true);
        this.setVisible(true);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
    }

    public void actionPerformed(ActionEvent e)
    {
        // Save function will be directed to the text file that looks like this (Example - 09753435028)
        if (e.getSource() == saveButton)
        {
            boolean emptyName = nameField.getText().isEmpty();
            boolean emptyContact = contactField.getText().isEmpty();
            String str = nameField.getText();
            String num = contactField.getText();

            if (emptyName || emptyContact)
            {
                JOptionPane.showConfirmDialog(null, "Please fill empty field", "Error when saving..",
                        JOptionPane.DEFAULT_OPTION);
            }

            if (!(emptyName || emptyContact))
            {
                try
                {
                    FileWriter fw = new FileWriter(fileName, true);
                    BufferedWriter writer = new BufferedWriter(fw);
                    {
                        writer.write(str + " - " + num);
                        writer.newLine();
                    }
                    writer.close();
                } catch (IOException ex) {
                    System.out.println("An error occurred while saving the contacts.");
                    ex.printStackTrace();
                }
                JOptionPane.showConfirmDialog(null, "Your response has been added successfully!", "Contact Saved",
                        JOptionPane.DEFAULT_OPTION);
                nameField.setText("");
                contactField.setText("");
            }
        }

        // Search Function only takes name then displays number associated with that name
        if (e.getSource() == searchButton)
        {
            boolean emptyName = nameField.getText().isEmpty();
            String str = nameField.getText();

            if (emptyName)
            {
                JOptionPane.showConfirmDialog(null, "Name cannot be empty to proceed", "Empty Name",
                        JOptionPane.DEFAULT_OPTION);
            }
            else
            {
                BufferedReader br;
                try
                {
                    br = new BufferedReader(new FileReader(fileName));
                } catch (FileNotFoundException ex)
                {
                    throw new RuntimeException(ex);
                }
                String line;
                boolean found = false;
                while (true) {
                    try
                    {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException ex)
                    {
                        throw new RuntimeException(ex);
                    }
                    String[] parts = line.split(" - ");
                    if (parts[0].equals(str)) {
                        found = true;
                        System.out.println(str + "'s phone number is " + parts[1]);
                        String phoneNumber = parts[1];
                        contactField.setText(phoneNumber);
                        JOptionPane.showConfirmDialog(null, str + "'s phone number is " + phoneNumber, "Phone Number Found" ,
                                JOptionPane.DEFAULT_OPTION);
                        break;
                    }
                }
                if (!found) {
                    JOptionPane.showConfirmDialog(null, "No phone number found for " + str, "Phone Number Not Found",
                            JOptionPane.DEFAULT_OPTION);
                    System.out.println("No phone number found for " + str);
                    nameField.setText("");
                }
                try
                {
                    br.close();
                } catch (IOException ex)
                {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}