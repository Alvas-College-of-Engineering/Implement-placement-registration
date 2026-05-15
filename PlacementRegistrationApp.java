import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * GUI application for placement registration.
 */
public class PlacementRegistrationApp extends JFrame {
    private final RegistrationSystem registrationSystem;
    private JTextField idField;
    private JTextField nameField;
    private JTextField emailField;
    private JTextField branchField;
    private JTextField cgpaField;
    private JTextField skillsField;
    private JTextField resumeField;
    private DefaultTableModel tableModel;
    private JList<String> driveList;
    private DefaultListModel<String> driveListModel;
    private JTextArea driveDetailsArea;
    private JList<String> companyList;
    private DefaultListModel<String> companyListModel;
    private JTextArea companyDetailsArea;
    private JLabel totalStudentsLabel;
    private JLabel eligibleCountLabel;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JLabel pageTitleLabel;
    private JLabel pageStepLabel;

    public PlacementRegistrationApp() {
        registrationSystem = new RegistrationSystem();
        setTitle("Placement Registration System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(980, 620);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setSystemAppearance();
        initializePlacementDrives();

        add(createBannerPanel(), BorderLayout.NORTH);
        add(createMainArea(), BorderLayout.CENTER);

        setVisible(true);
        showCard("registration");
    }

    private void setSystemAppearance() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }
    }

    private JPanel createBannerPanel() {
        JPanel banner = new JPanel(new BorderLayout(12, 12));
        banner.setBackground(new Color(30, 136, 229));
        banner.setBorder(BorderFactory.createEmptyBorder(14, 18, 14, 18));

        JLabel logoLabel = new JLabel(createLogoIcon(new Color(255, 255, 255), "P"));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 12));

        JPanel textPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        textPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("Placement Registration Hub");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        JLabel subtitleLabel = new JLabel("Register students, review company drives and track eligible candidates.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(227, 242, 253));
        textPanel.add(titleLabel);
        textPanel.add(subtitleLabel);

        banner.add(logoLabel, BorderLayout.WEST);
        banner.add(textPanel, BorderLayout.CENTER);

        return banner;
    }

    private JPanel createMainArea() {
         JPanel mainArea = new JPanel(new BorderLayout()) {
    ImageIcon bgIcon = new ImageIcon("images/image.jpg");


private Image backgroundImage = bgIcon.getImage();
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
};

mainArea.setOpaque(false);
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 250, 255));
        header.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        pageTitleLabel = new JLabel("Student Registration");
        pageTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        pageTitleLabel.setForeground(new Color(24, 81, 156));

        pageStepLabel = new JLabel("Step 1 of 3");
        pageStepLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pageStepLabel.setForeground(new Color(76, 175, 80));

        header.add(pageTitleLabel, BorderLayout.WEST);
        header.add(pageStepLabel, BorderLayout.EAST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(new Color(245, 250, 255));
        cardPanel.add(createRegistrationPanel(), "registration");
        cardPanel.add(createRegisteredStudentsPagePanel(), "students");
        cardPanel.add(createDashboardPanel(), "dashboard");

        mainArea.add(header, BorderLayout.NORTH);
        mainArea.add(cardPanel, BorderLayout.CENTER);
        return mainArea;
    }

    private void showCard(String name) {
        cardLayout.show(cardPanel, name);
        switch (name) {
            case "registration":
                pageTitleLabel.setText("Student Registration");
                pageStepLabel.setText("Step 1 of 3");
                break;
            case "students":
                pageTitleLabel.setText("Registered Students");
                pageStepLabel.setText("Step 2 of 3");
                updateStudentTable();
                break;
            case "dashboard":
                pageTitleLabel.setText("Placement Dashboard");
                pageStepLabel.setText("Step 3 of 3");
                if (companyList != null && companyList.getSelectedValue() == null && companyList.getModel().getSize() > 0) {
                    companyList.setSelectedIndex(0);
                }
                refreshDashboard();
                break;
        }
    }

    private JPanel createNavigationPanel(String previousCard, String nextCard) {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        navPanel.setBackground(new Color(245, 250, 255));

        if (previousCard != null) {
            JButton prevButton = new JButton("Previous");
            prevButton.setBackground(new Color(224, 224, 224));
            prevButton.setForeground(new Color(33, 33, 33));
            prevButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            prevButton.setOpaque(true);
            prevButton.setBorderPainted(false);
            prevButton.addActionListener(e -> showCard(previousCard));
            navPanel.add(prevButton);
        }

        if (nextCard != null) {
            JButton nextButton = new JButton("Next");
            nextButton.setBackground(new Color(30, 136, 229));
            nextButton.setForeground(Color.WHITE);
            nextButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
            nextButton.setOpaque(true);
            nextButton.setBorderPainted(false);
            nextButton.addActionListener(e -> showCard(nextCard));
            navPanel.add(nextButton);
        }

        return navPanel;
    }

    private Icon createLogoIcon(Color background, String text) {
        int size = 30;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(background);
        g.fillOval(0, 0, size, size);
        g.setColor(new Color(255, 255, 255, 220));
        g.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g.getFontMetrics();
        int x = (size - fm.stringWidth(text)) / 2;
        int y = (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, x, y);
        g.dispose();
        return new ImageIcon(image);
    }

    private JPanel createRegistrationPanel() {
        JPanel container = new JPanel(new BorderLayout(0, 12));
        container.setBackground(new Color(235, 248, 255));
        container.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel formPanel = createFormPanel();
        formPanel.setBackground(new Color(255, 255, 255));
        container.add(formPanel, BorderLayout.CENTER);
        container.add(createNavigationPanel(null, "students"), BorderLayout.SOUTH);
        return container;
    }

    private JPanel createDriveOverviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(235, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setBackground(new Color(255, 255, 255));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Placement Drives")));

        driveListModel = new DefaultListModel<>();
        driveList = new JList<>(driveListModel);
        driveList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        driveList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        driveList.setBackground(new Color(245, 250, 255));
        driveList.setSelectionBackground(new Color(200, 230, 255));
        driveList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                showDriveDetails();
            }
        });

        leftPanel.add(new JScrollPane(driveList), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBackground(new Color(255, 255, 255));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Drive Details")));

        driveDetailsArea = new JTextArea();
        driveDetailsArea.setEditable(false);
        driveDetailsArea.setBackground(new Color(245, 248, 255));
        driveDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        driveDetailsArea.setLineWrap(true);
        driveDetailsArea.setWrapStyleWord(true);
        driveDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(new JScrollPane(driveDetailsArea), BorderLayout.CENTER);

        panel.add(leftPanel, BorderLayout.WEST);
        panel.add(rightPanel, BorderLayout.CENTER);
        panel.add(createNavigationPanel(null, "registration"), BorderLayout.SOUTH);

        return panel;
    }

    private void showDriveDetails() {
        String selectedDrive = driveList.getSelectedValue();
        if (selectedDrive == null) {
            driveDetailsArea.setText("Select a drive to view eligibility details.");
            return;
        }
        PlacementDrive drive = registrationSystem.getPlacementDrive(selectedDrive);
        if (drive == null) {
            driveDetailsArea.setText("No details available for the selected drive.");
            return;
        }
        StringBuilder details = new StringBuilder();
        details.append("Company: ").append(drive.getCompanyName()).append("\n")
                .append("Drive Date: ").append(drive.getDriveDate()).append("\n")
                .append("Minimum CGPA: ").append(drive.getMinimumCgpa()).append("\n")
                .append("Allowed Branches: ").append(String.join(", ", drive.getAllowedBranches())).append("\n")
                .append("Required Skills: ").append(String.join(", ", drive.getRequiredSkills())).append("\n");
        driveDetailsArea.setText(details.toString());
    }

    private JPanel createRegisteredStudentsPagePanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(new Color(235, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel tablePanel = createTablePanel();
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(createNavigationPanel("registration", "dashboard"), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createDashboardPanel() {
        JPanel container = new JPanel(new BorderLayout(12, 12));
        container.setBackground(new Color(235, 245, 255));
        container.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel leftPanel = new JPanel(new BorderLayout(8, 8));
        leftPanel.setBackground(new Color(255, 255, 255));
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Available Placement Drives")));

        companyListModel = new DefaultListModel<>();
        companyList = new JList<>(companyListModel);
        companyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        companyList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        companyList.setBackground(new Color(245, 250, 255));
        companyList.setSelectionBackground(new Color(200, 230, 255));
        companyList.addListSelectionListener(new CompanySelectionListener());

        leftPanel.add(new JScrollPane(companyList), BorderLayout.CENTER);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 8, 8));
        infoPanel.setBackground(new Color(245, 250, 255));
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Summary")));
        totalStudentsLabel = new JLabel("Total Registered Students: 0");
        totalStudentsLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        eligibleCountLabel = new JLabel("Eligible Students for selected company: 0");
        eligibleCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        infoPanel.add(totalStudentsLabel);
        infoPanel.add(eligibleCountLabel);
        leftPanel.add(infoPanel, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout(8, 8));
        rightPanel.setBackground(new Color(255, 255, 255));
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Company Eligibility Details")));
        companyDetailsArea = new JTextArea();
        companyDetailsArea.setEditable(false);
        companyDetailsArea.setBackground(new Color(245, 248, 255));
        companyDetailsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        companyDetailsArea.setLineWrap(true);
        companyDetailsArea.setWrapStyleWord(true);
        companyDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rightPanel.add(new JScrollPane(companyDetailsArea), BorderLayout.CENTER);

        container.add(leftPanel, BorderLayout.WEST);
        container.add(rightPanel, BorderLayout.CENTER);
        container.add(createNavigationPanel("students", null), BorderLayout.SOUTH);

        refreshDashboard();
        return container;
    }

    private JPanel createFormPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(520, 700));
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Student Registration")));

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(new Color(255, 255, 255));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Register Student");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 20));
        heading.setForeground(new Color(30, 136, 229));
        gbc.gridwidth = 2;
        fieldsPanel.add(heading, gbc);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setFont(labelFont);
        idLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(idLabel, gbc);
        idField = new JTextField(24);
        idField.setPreferredSize(new Dimension(320, 32));
        idField.setBackground(new Color(245, 248, 255));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(idField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(labelFont);
        nameLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(nameLabel, gbc);
        nameField = new JTextField(24);
        nameField.setPreferredSize(new Dimension(320, 32));
        nameField.setBackground(new Color(245, 248, 255));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(nameField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(labelFont);
        emailLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(emailLabel, gbc);
        emailField = new JTextField(24);
        emailField.setPreferredSize(new Dimension(320, 32));
        emailField.setBackground(new Color(245, 248, 255));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(emailField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setFont(labelFont);
        branchLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(branchLabel, gbc);
        branchField = new JTextField(24);
        branchField.setPreferredSize(new Dimension(320, 32));
        branchField.setBackground(new Color(245, 248, 255));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(branchField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel cgpaLabel = new JLabel("CGPA:");
        cgpaLabel.setFont(labelFont);
        cgpaLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(cgpaLabel, gbc);
        cgpaField = new JTextField(24);
        cgpaField.setPreferredSize(new Dimension(320, 32));
        cgpaField.setBackground(new Color(245, 248, 255));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(cgpaField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel skillsLabel = new JLabel("Skills:");
        skillsLabel.setFont(labelFont);
        skillsLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(skillsLabel, gbc);
        skillsField = new JTextField(24);
        skillsField.setPreferredSize(new Dimension(320, 32));
        skillsField.setBackground(new Color(245, 248, 255));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(skillsField, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        JLabel resumeLabel = new JLabel("Resume (.pdf):");
        resumeLabel.setFont(labelFont);
        resumeLabel.setForeground(new Color(55, 71, 79));
        fieldsPanel.add(resumeLabel, gbc);

        JPanel resumePanel = new JPanel(new BorderLayout(8, 0));
        resumePanel.setOpaque(false);
        resumeField = new JTextField();
        resumeField.setEditable(false);
        resumeField.setBackground(new Color(245, 248, 255));
        resumeField.setPreferredSize(new Dimension(250, 32));
        JButton browseButton = new JButton("Browse");
        browseButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        browseButton.setBackground(new Color(30, 136, 229));
        browseButton.setForeground(Color.WHITE);
        browseButton.setOpaque(true);
        browseButton.setBorderPainted(false);
        browseButton.addActionListener(e -> chooseResumeFile());
        resumePanel.add(resumeField, BorderLayout.CENTER);
        resumePanel.add(browseButton, BorderLayout.EAST);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        fieldsPanel.add(resumePanel, gbc);

        mainPanel.add(new JScrollPane(fieldsPanel), BorderLayout.CENTER);

        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(new Color(0, 150, 136));
        registerButton.setForeground(Color.WHITE);
        registerButton.setOpaque(true);
        registerButton.setBorderPainted(false);
        registerButton.addActionListener(new RegisterButtonListener());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(255, 255, 255));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(registerButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private void chooseResumeFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Resume PDF");
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileNameExtensionFilter("PDF Documents", "pdf"));
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();
            if (selectedFile != null && selectedFile.getName().toLowerCase().endsWith(".pdf")) {
                resumeField.setText(selectedFile.getAbsolutePath());
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a PDF file.", "Invalid File", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(247, 250, 255));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 220, 240), 1),
                BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(30, 136, 229), 1), "Registered Students")));

        String[] columnNames = {"ID", "Name", "Email", "Branch", "CGPA", "Skills", "Resume"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable studentTable = new JTable(tableModel);
        studentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        studentTable.setRowHeight(26);
        studentTable.setSelectionBackground(new Color(200, 230, 255));
        studentTable.setGridColor(new Color(220, 230, 240));
        studentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        studentTable.getTableHeader().setBackground(new Color(240, 248, 255));
        studentTable.getTableHeader().setForeground(new Color(33, 33, 33));

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.rowAtPoint(e.getPoint());
                int col = studentTable.columnAtPoint(e.getPoint());
                if (col == 6 && row >= 0) {
                    Student student = registrationSystem.getAllStudents().get(row);
                    String path = student.getResumePath();
                    if (path != null && !path.isEmpty()) {
                        try {
                            if (Desktop.isDesktopSupported()) {
                                Desktop.getDesktop().open(new File(path));
                            } else {
                                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                                        "Desktop not supported for opening files.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                                    "Failed to open resume: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void initializePlacementDrives() {
        registrationSystem.addPlacementDrive(new PlacementDrive("TechCorp", 7.0,
                Arrays.asList("CS", "IT", "ECE"), Arrays.asList("Java", "SQL", "Problem Solving"), LocalDate.now().plusDays(14)));
        registrationSystem.addPlacementDrive(new PlacementDrive("DataWorks", 8.0,
                Arrays.asList("CS", "IT"), Arrays.asList("Python", "Data Structures", "Machine Learning"), LocalDate.now().plusDays(21)));
        registrationSystem.addPlacementDrive(new PlacementDrive("FinSys", 6.5,
                Arrays.asList("CS", "IT", "ECE", "MECH"), Arrays.asList("C++", "Analytics", "Financial Modeling"), LocalDate.now().plusDays(30)));
        registrationSystem.addPlacementDrive(new PlacementDrive("CloudSphere", 7.5,
                Arrays.asList("CS", "IT", "ECE"), Arrays.asList("Cloud Computing", "AWS", "Docker"), LocalDate.now().plusDays(18)));
        registrationSystem.addPlacementDrive(new PlacementDrive("HealthPulse", 6.0,
                Arrays.asList("ECE", "IT", "BIOTECH"), Arrays.asList("Data Analysis", "Algorithms", "Healthcare Domain"), LocalDate.now().plusDays(25)));
        registrationSystem.addPlacementDrive(new PlacementDrive("AutoDrive", 6.8,
                Arrays.asList("MECH", "ECE", "CS"), Arrays.asList("Embedded Systems", "Automation", "Python"), LocalDate.now().plusDays(28)));
        registrationSystem.addPlacementDrive(new PlacementDrive("Google", 8.5,
                Arrays.asList("CS", "IT"), Arrays.asList("Java", "System Design", "Algorithms"), LocalDate.now().plusDays(35)));
        registrationSystem.addPlacementDrive(new PlacementDrive("Microsoft", 8.3,
                Arrays.asList("CS", "IT"), Arrays.asList("C#", ".NET", "Cloud Services"), LocalDate.now().plusDays(33)));
        registrationSystem.addPlacementDrive(new PlacementDrive("Oracle", 7.8,
                Arrays.asList("CS", "IT", "ECE"), Arrays.asList("SQL", "Database Design", "Java"), LocalDate.now().plusDays(22)));

        if (driveListModel != null) {
            driveListModel.clear();
            for (PlacementDrive drive : registrationSystem.getAllPlacementDrives()) {
                driveListModel.addElement(drive.getCompanyName());
            }
        }
        if (companyListModel != null) {
            companyListModel.clear();
            for (PlacementDrive drive : registrationSystem.getAllPlacementDrives()) {
                companyListModel.addElement(drive.getCompanyName());
            }
        }
    }

    private void refreshDashboard() {
        if (companyListModel != null) {
            companyListModel.clear();
            for (PlacementDrive drive : registrationSystem.getAllPlacementDrives()) {
                companyListModel.addElement(drive.getCompanyName());
            }
        }
        totalStudentsLabel.setText("Total Registered Students: " + registrationSystem.getAllStudents().size());
        updateSelectedCompanyInfo();
    }

    private void updateSelectedCompanyInfo() {
        if (companyList == null) {
            return;
        }
        String selectedCompany = companyList.getSelectedValue();
        if (selectedCompany == null && !registrationSystem.getAllPlacementDrives().isEmpty()) {
            companyList.setSelectedIndex(0);
            selectedCompany = companyList.getSelectedValue();
        }
        if (selectedCompany == null) {
            companyDetailsArea.setText("Select a company to view eligibility details.");
            eligibleCountLabel.setText("Eligible Students for selected company: 0");
            return;
        }

        PlacementDrive drive = registrationSystem.getPlacementDrive(selectedCompany);
        if (drive == null) {
            companyDetailsArea.setText("No details available for the selected drive.");
            eligibleCountLabel.setText("Eligible Students for selected company: 0");
            return;
        }

        List<Student> eligibleStudents = registrationSystem.getEligibleStudents(selectedCompany);
        StringBuilder details = new StringBuilder();
        details.append("Company: ").append(drive.getCompanyName()).append("\n")
                .append("Drive Date: ").append(drive.getDriveDate()).append("\n")
                .append("Minimum CGPA: ").append(drive.getMinimumCgpa()).append("\n")
                .append("Allowed Branches: ").append(String.join(", ", drive.getAllowedBranches())).append("\n")
                .append("Required Skills: ").append(String.join(", ", drive.getRequiredSkills())).append("\n\n")
                .append("Eligible Students (" ).append(eligibleStudents.size()).append("):\n");

        if (eligibleStudents.isEmpty()) {
            details.append("No students currently eligible for this drive.");
        } else {
            for (Student student : eligibleStudents) {
                details.append(student.getStudentId()).append(" - ")
                        .append(student.getName()).append(" (Branch: ")
                        .append(student.getBranch()).append(", CGPA: ")
                        .append(student.getCgpa()).append(")\n");
            }
        }

        companyDetailsArea.setText(details.toString());
        eligibleCountLabel.setText("Eligible Students for selected company: " + eligibleStudents.size());
    }

    private void updateStudentTable() {
        tableModel.setRowCount(0);
        for (Student student : registrationSystem.getAllStudents()) {
            String skills = String.join(", ", student.getSkills());
            String resume = student.getResumePath() == null || student.getResumePath().isEmpty()
                    ? "None"
                    : new File(student.getResumePath()).getName();
            Object[] row = {student.getStudentId(), student.getName(), student.getEmail(), student.getBranch(), student.getCgpa(), skills, resume};
            tableModel.addRow(row);
        }
        refreshDashboard();
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String studentId = idField.getText().trim();
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String branch = branchField.getText().trim();
            String cgpaText = cgpaField.getText().trim();
            String skillsText = skillsField.getText().trim();
            String resumeText = resumeField.getText().trim();

            if (studentId.isEmpty() || name.isEmpty() || email.isEmpty() || branch.isEmpty() || cgpaText.isEmpty() || resumeText.isEmpty()) {
                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                        "Please fill in all required fields, including resume.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                        "Please enter a valid email address.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double cgpa;
            try {
                cgpa = Double.parseDouble(cgpaText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                        "CGPA must be a valid number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cgpa < 0.0 || cgpa > 10.0) {
                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                        "CGPA must be between 0.0 and 10.0.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!resumeText.toLowerCase().endsWith(".pdf")) {
                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                        "Resume must be a PDF file.", "Validation Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> skills = Arrays.asList(skillsText.split("\\s*,\\s*"));
            Student student = new Student(studentId, name, email, branch, cgpa, skills, resumeText);

            if (!registrationSystem.registerStudent(student)) {
                JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                        "Student ID already exists or registration failed.", "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            updateStudentTable();
            clearFormFields();
            JOptionPane.showMessageDialog(PlacementRegistrationApp.this,
                    "Student registered successfully.", "Registration Complete", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class CompanySelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                updateSelectedCompanyInfo();
            }
        }
    }

    private void clearFormFields() {
        idField.setText("");
        nameField.setText("");
        emailField.setText("");
        branchField.setText("");
        cgpaField.setText("");
        skillsField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PlacementRegistrationApp::new);
    }
}
