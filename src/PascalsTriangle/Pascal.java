/*
 * Copyright (C) 2018 Ryan Castelli
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package PascalsTriangle;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Pascal's Triangle in a JFrame. Credit to Michał Schielmann on StackOverflow
 * for assistance on number spacing! Michał:
 * http://stackoverflow.com/users/2169264/micha%C5%82-schielmann
 *
 * @author NTropy
 * @version 1.0
 */
public class Pascal extends JFrame {

    private static ActionEvent sendOverride;

    private static Integer numberOfRows;

    private static JButton jbtnSend;

    private static JFrame jfrm;

    private static JTextArea jtaDisplay;

    private static JTextField jtfInput;

    private static JScrollPane jscrlp;

    private static String input;

    /**
     * Creates JFrame
     * @param args 
     */
    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            jfrm = new Pascal();
        });
    }

    /**
     * Initializes JFrame
     */
    private Pascal() {
        try {
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException exe) {
            System.err.println("Nimbus unavailable");
        }
        setLayout(new BorderLayout()); //sets layout based on borders

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //gets screen dimensions

        int screenWidth = (int) screenSize.getWidth(); //width of screen
        int screenHeight = (int) screenSize.getHeight(); //height of screen

        setSize(screenWidth - 50, screenHeight - 50); //sets size

        setLocationRelativeTo(null);

        jtaDisplay = new JTextArea(20, 30); //size of display
        jtaDisplay.setEditable(false); //display not editable
        jtaDisplay.setLineWrap(true); //lines wrap down

        jscrlp = new JScrollPane(jtaDisplay); //makes dispaly scrollable

        jtfInput = new JTextField(30); //sets character width of input field

        jbtnSend = new JButton("Send"); //sets button text

        jbtnSend.addActionListener(new handler()); //adds listener to button

        KeyListener key = new handler(); //adds handler for 'enter' key

        jtfInput.addKeyListener(key); //adds keylistener for 'enter'
        add(jscrlp, BorderLayout.PAGE_START); //adds scrollable display to main frame

        sendOverride = new ActionEvent(jbtnSend, 1001, "Send"); //allows key to trigger same method as button

        JPanel p1 = new JPanel(); //panel for input/button

        p1.setLayout(new FlowLayout()); //flow layout for input/button
        p1.add(jtfInput, BorderLayout.LINE_END); //adds input to panel
        p1.add(jbtnSend, BorderLayout.LINE_END); //adds button to panel

        add(p1, BorderLayout.PAGE_END); //add button/input to main frame

        setVisible(true); //makes frame visible

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //kills application on close

        jtaDisplay.setText("Please enter number of rows for Pascals Triangle: ");
    }

    /**
     * Ensures input is in range
     * @param input
     * @return 
     */
    private static boolean validateInput(final String input) {
        try {
            Integer inputValue = Integer.parseInt(input);
            if (inputValue > 2 && inputValue < 22) {
                numberOfRows = inputValue;
                return true;
            }

            jtaDisplay.setText(jtaDisplay.getText() + "\nValue must be an integer between 3 and 21. Please insert valid number: ");
            return false;
        } catch (final NumberFormatException e) {
            jtaDisplay.setText(jtaDisplay.getText() + "\nError while parsing input. Please insert valid number: ");
        }
        return false;
    }

    /**
     * Prints actual triangle
     */
    private static void makeTriangle() {
        int maxNumberLength = Double.toString(Math.pow(2d, numberOfRows.doubleValue())).length();

        for (int i = 0; i < numberOfRows; i++) {
            String spaces = "";
            int counter = (maxNumberLength * (numberOfRows - i)) / 2;
            for (int f = counter; f > 0; f--) {
                spaces += " ";
            }

            jtaDisplay.setText(jtaDisplay.getText() + spaces);
            for (int j = 0; j <= i; j++) {
                long number = ncr(i, j);
                jtaDisplay.setText(jtaDisplay.getText() + number + spaces(number, maxNumberLength));
            }
            jtaDisplay.setText(jtaDisplay.getText() + "\n");
        }
    }

    /**
     * Finds number of spaces needed
     * @param number
     * @param maxNumberLength
     * @return number of spaces
     */
    private static String spaces(final Long number, final int maxNumberLength) {
        StringBuilder spaces = new StringBuilder("");
        for (int i = 0; i < maxNumberLength - number.toString().length(); i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

    /**
     * Next row of numbers
     * @param n
     * @param r
     * @return next row
     */
    private static long ncr(int n, int r) {
        return fact(n) / (fact(r) * fact(n - r));
    }

    /**
     * Finds number
     * @param n
     * @return next number
     */
    private static long fact(int n) {
        long ans = 1;
        for (int i = 2; i <= n; i++) {
            ans *= i;
        }
        return ans;
    }

    /**
     * Handles sending to server on button press
     */
    private static class handler implements ActionListener, KeyListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            boolean validNumber;
            
            jtaDisplay.setText(jtaDisplay.getText() + "\n");
            
            do {
                input = jtfInput.getText();
                validNumber = validateInput(input);
            } while (!validNumber);

            try {
                Integer inputValue = Integer.parseInt(input);
                if (inputValue > 2 && inputValue < 22) {
                    numberOfRows = inputValue;
                }
            } catch (final NumberFormatException e) {
                jtaDisplay.setText(jtaDisplay.getText() + "\nError while parsing input. Please insert valid number: ");
            }

            makeTriangle();

            jtaDisplay.setText(jtaDisplay.getText() + "\nPlease enter number of rows for Pascals Triangle: ");

            jtfInput.setText("");
            jfrm.repaint();

        }

        /**
         * Invokes handler for server communication
         *
         * @param e
         */
        @Override
        public void keyPressed(KeyEvent e) //see comments from above
        {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                actionPerformed(sendOverride);
            }
        }

        /**
         * Necessary override, does nothing
         *
         * @param e
         */
        @Override
        public void keyReleased(KeyEvent e) {
        }

        /**
         * Necessary override, does nothing
         *
         * @param e
         */
        @Override
        public void keyTyped(KeyEvent e) {
        }
    }
}
