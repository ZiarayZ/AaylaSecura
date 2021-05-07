import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.AncestorEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class TaskLogUI extends JFrame {

	private JPanel contentPane;
	private JTable taskListTable;
	private JTextField caretakerNameField;
	private JTextField timeCompletedField;
	private LogTasks taskLog;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TaskLogUI frame = new TaskLogUI(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TaskLogUI(LogTasks loggingTask) {
		taskLog = loggingTask;
		setTitle("Log Tasks");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 976, 686);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(255, 255, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblHeadingLabel = new JLabel("Please Log a Completed Task");
		lblHeadingLabel.setForeground(new Color(105, 105, 105));
		lblHeadingLabel.setOpaque(true);
		lblHeadingLabel.setFont(new Font("Verdana", Font.PLAIN, 28));
		lblHeadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblHeadingLabel.setBackground(new Color(224, 255, 255));
		lblHeadingLabel.setBounds(256, 55, 444, 67);
		contentPane.add(lblHeadingLabel);
		
		taskListTable = new JTable();
		taskListTable.setBackground(new Color(192, 192, 192));
		taskListTable.setBounds(68, 150, 820, 232);
		contentPane.add(taskListTable);
		
		caretakerNameField = new JTextField();
		caretakerNameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//record caretaker name here
			}
		});
		caretakerNameField.setBackground(new Color(112, 128, 144));
		caretakerNameField.setBounds(261, 427, 118, 20);
		contentPane.add(caretakerNameField);
		caretakerNameField.setColumns(10);
		
		JLabel lblNameLabel = new JLabel("Completed By");
		lblNameLabel.setBounds(179, 430, 72, 14);
		contentPane.add(lblNameLabel);
		
		timeCompletedField = new JTextField();
		timeCompletedField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//record when the task was completed
			}
		});
		timeCompletedField.setBackground(new Color(112, 128, 144));
		timeCompletedField.setBounds(261, 473, 118, 20);
		contentPane.add(timeCompletedField);
		timeCompletedField.setColumns(10);
		
		JLabel lblTimeLabel = new JLabel("Time Completed");
		lblTimeLabel.setBounds(179, 476, 85, 14);
		contentPane.add(lblTimeLabel);
		
		JTextArea addCommentsField = new JTextArea();
		//need to find out how to handle input in a text area
		addCommentsField.setBackground(new Color(112, 128, 144));
		addCommentsField.setBounds(641, 425, 213, 68);
		contentPane.add(addCommentsField);
		
		JLabel lblNewLabel = new JLabel("Additional Comments");
		lblNewLabel.setBounds(513, 430, 118, 14);
		contentPane.add(lblNewLabel);
		
		JButton sortByCaretakerButton = new JButton("Sort By Caretaker");
		sortByCaretakerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//sort db by caretaker here
			}
		});
		sortByCaretakerButton.setBounds(78, 553, 125, 37);
		contentPane.add(sortByCaretakerButton);
		
		JButton sortByDeadlineButton = new JButton("Sort By Deadline");
		sortByDeadlineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//sort db by deadline here
			}
		});
		sortByDeadlineButton.setBounds(213, 553, 132, 37);
		contentPane.add(sortByDeadlineButton);
		
		JButton editCompletedButton = new JButton("Edit Completed Task");
		editCompletedButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//detects a selected task to edit here
			}
		});
		editCompletedButton.setBounds(355, 553, 133, 37);
		contentPane.add(editCompletedButton);
		
		JButton logTaskButton = new JButton("Log Task");
		logTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//logs the task
			}
		});
		logTaskButton.setBounds(498, 553, 118, 37);
		contentPane.add(logTaskButton);
		
		JButton undoLoggedTaskButton = new JButton("Undo Changes");
		undoLoggedTaskButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//undo functionality (optional)
			}
		});
		undoLoggedTaskButton.setBounds(626, 553, 124, 37);
		contentPane.add(undoLoggedTaskButton);
		
		JButton createReportButton = new JButton("Create Report");
		createReportButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//generates the report
			}
		});
		createReportButton.setBounds(760, 553, 128, 37);
		contentPane.add(createReportButton);
	}
}
