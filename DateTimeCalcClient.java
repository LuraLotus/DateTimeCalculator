import java.io.*;
import java.net.*;
import java.time.*;
import java.time.format.*;
import java.time.temporal.*;
import java.util.*;
import javax.swing.*;
import java.awt.event.*;
import java.text.*;

class Client
{
    public static void main(String[] args) throws Exception
    {
        JFrame frame = new JFrame();
        JSpinner datepicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(datepicker, "MM/dd/yyyy");
        datepicker.setEditor(dateEditor);
        datepicker.setBounds(80, 40, 100, 25);

        JSpinner timepicker = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timepicker, "HH:mm");
        timepicker.setEditor(timeEditor);
        timepicker.setValue(new Date());
        timepicker.setBounds(190, 40, 100, 25);

        JLabel label = new JLabel("Remaining time:");
        label.setBounds(65, 100, 100, 30);

        JLabel output = new JLabel("");
        output.setBounds(160, 100, 170, 30);

        JButton submit = new JButton("Submit");
        submit.setBounds(130, 70, 100, 30);
        submit.addActionListener(new ActionListener()
        {
          @Override
          public void actionPerformed(ActionEvent e)
          {
            try
            {
              datepicker.commitEdit();
              timepicker.commitEdit();
            }
            catch(java.text.ParseException x) {}

            DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
            String tempDate = new SimpleDateFormat("MM/dd/yyyy").format(datepicker.getValue());
            String tempTime = new SimpleDateFormat("HH:mm").format(timepicker.getValue());
            String userInput = tempDate + " " + tempTime;
            LocalDateTime serverDateTime = null;
            try
            {
              serverDateTime = getServerDateTime();
            } catch(Exception ex) { ex.printStackTrace(); }

            LocalDateTime temp = LocalDateTime.from(serverDateTime);
            LocalDateTime userDateTime = LocalDateTime.parse(userInput, format);

            long days = temp.until(userDateTime, ChronoUnit.DAYS);
            temp = temp.plusDays(days);

            long hours = temp.until(userDateTime, ChronoUnit.HOURS);
            temp = temp.plusHours(hours);

            long minutes = temp.until(userDateTime, ChronoUnit.MINUTES);
            temp = temp.plusMinutes(minutes);
            output.setText(days + " days " + hours + " hours " + minutes + " minutes");
          }
        });

        frame.add(datepicker);
        frame.add(timepicker);
        frame.add(submit);
        frame.add(label);
        frame.add(output);
        frame.setSize(400, 200);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static LocalDateTime getServerDateTime() throws Exception
    {
      Socket socket = new Socket(InetAddress.getLocalHost(), 21513);
      ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
      LocalDateTime serverDateTime = (LocalDateTime) in.readObject();

      return serverDateTime;
    }
}
