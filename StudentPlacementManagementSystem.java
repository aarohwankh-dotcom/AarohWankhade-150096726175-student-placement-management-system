import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * Case Study 31: Student Placement Management System
 * Course: Java Programming (Semester III - B.Tech CSE 2025-29)
 * Student: Aaroh Wankhade
 * 
 * Technology: Pure Core Java + Java Swing + Java Collections Framework
 * Concepts Demonstrated:
 *  - Classes & Objects: Student, Company, Job, Application, Interview
 *  - Constructors: Initializing objects with 'this' keyword
 *  - Arrays: Skills in Student, Required Skills in Job
 *  - ArrayList: Storing Student and Company records
 *  - LinkedList: Storing Application history and Interview schedules
 *  - HashMap: Mapping Student ID -> Student object (O(1) search)
 *  - TreeMap: Organizing Jobs sorted by salary/package
 *  - CRUD: Create, Read, Update, Delete for Students, Companies, Jobs
 *  - Searching: By ID or name/title using loops and maps
 *  - Sorting: Students by CGPA (Bubble Sort) & Jobs by Salary (TreeMap)
 *  - Eligibility Checking: Minimum CGPA + required skills array matching
 *  - Exception Handling: User-defined exceptions (InvalidApplicationException, InvalidInterviewException)
 *  - Validation: Form input checking with JOptionPane alerts
 *  - Swing GUI: Tabbed desktop application with JTable and action listeners
 */

// =========================================================================
// CUSTOM EXCEPTION CLASSES (Module VI: User-Defined Exceptions)
// =========================================================================

/**
 * Exception thrown when an application cannot be processed due to
 * ineligibility, duplicate submission, or missing references.
 */
class InvalidApplicationException extends Exception {
    public InvalidApplicationException(String message) {
        super(message);
    }
}

/**
 * Exception thrown when an interview cannot be scheduled due to
 * invalid application status, missing fields, or non-existent application.
 */
class InvalidInterviewException extends Exception {
    public InvalidInterviewException(String message) {
        super(message);
    }
}

// =========================================================================
// DATA MODEL CLASSES (Module IV: Classes, Objects, Encapsulation)
// =========================================================================

/**
 * Represents a student candidate in the placement system.
 * Uses an Array of Strings for storing skills.
 */
class Student {
    private String studentId;
    private String name;
    private double cgpa;
    private String email;
    private String phone;
    private String[] skills; // Array requirement from case study

    // Parameterized Constructor
    public Student(String studentId, String name, double cgpa, String email, String phone, String[] skills) {
        this.studentId = studentId;
        this.name = name;
        this.cgpa = cgpa;
        this.email = email;
        this.phone = phone;
        this.skills = skills;
    }

    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String[] getSkills() { return skills; }
    public void setSkills(String[] skills) { this.skills = skills; }

    // Helper method to format skills array into a comma-separated String
    public String getSkillsFormatted() {
        if (skills == null || skills.length == 0) return "None";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < skills.length; i++) {
            sb.append(skills[i]);
            if (i < skills.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    // Check if the student possesses a given skill (case-insensitive)
    public boolean hasSkill(String requiredSkill) {
        if (skills == null) return false;
        for (int i = 0; i < skills.length; i++) {
            if (skills[i].trim().equalsIgnoreCase(requiredSkill.trim())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return studentId + " - " + name + " (CGPA: " + cgpa + ")";
    }
}

/**
 * Represents a recruiting company.
 */
class Company {
    private String companyId;
    private String companyName;
    private String location;
    private String industry;

    // Parameterized Constructor
    public Company(String companyId, String companyName, String location, String industry) {
        this.companyId = companyId;
        this.companyName = companyName;
        this.location = location;
        this.industry = industry;
    }

    // Getters and Setters
    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }

    @Override
    public String toString() {
        return companyId + " - " + companyName + " (" + location + ")";
    }
}

/**
 * Represents a job opening posted by a company.
 * Uses an Array of Strings for required skills.
 */
class Job {
    private String jobId;
    private String companyId;
    private String jobTitle;
    private double salary; // Package in LPA (Lakhs Per Annum)
    private double minimumCGPA;
    private String[] requiredSkills; // Array requirement from case study

    // Parameterized Constructor
    public Job(String jobId, String companyId, String jobTitle, double salary, double minimumCGPA, String[] requiredSkills) {
        this.jobId = jobId;
        this.companyId = companyId;
        this.jobTitle = jobTitle;
        this.salary = salary;
        this.minimumCGPA = minimumCGPA;
        this.requiredSkills = requiredSkills;
    }

    // Getters and Setters
    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getCompanyId() { return companyId; }
    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getJobTitle() { return jobTitle; }
    public void setJobTitle(String jobTitle) { this.jobTitle = jobTitle; }

    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }

    public double getMinimumCGPA() { return minimumCGPA; }
    public void setMinimumCGPA(double minimumCGPA) { this.minimumCGPA = minimumCGPA; }

    public String[] getRequiredSkills() { return requiredSkills; }
    public void setRequiredSkills(String[] requiredSkills) { this.requiredSkills = requiredSkills; }

    // Helper method to format required skills into a comma-separated String
    public String getRequiredSkillsFormatted() {
        if (requiredSkills == null || requiredSkills.length == 0) return "None";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < requiredSkills.length; i++) {
            sb.append(requiredSkills[i]);
            if (i < requiredSkills.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return jobId + " - " + jobTitle + " (" + salary + " LPA)";
    }
}

/**
 * Represents a student application for a specific job opening.
 * Status values: Applied, Shortlisted, Selected, Rejected
 */
class Application {
    private String applicationId;
    private String studentId;
    private String jobId;
    private String status; // "Applied", "Shortlisted", "Selected", "Rejected"

    // Parameterized Constructor
    public Application(String applicationId, String studentId, String jobId, String status) {
        this.applicationId = applicationId;
        this.studentId = studentId;
        this.jobId = jobId;
        this.status = status;
    }

    // Getters and Setters
    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getJobId() { return jobId; }
    public void setJobId(String jobId) { this.jobId = jobId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return applicationId + " [Student: " + studentId + ", Job: " + jobId + ", Status: " + status + "]";
    }
}

/**
 * Represents an interview scheduled for a shortlisted application.
 * Mode: Online / Offline
 */
class Interview {
    private String interviewId;
    private String applicationId;
    private String date; // YYYY-MM-DD
    private String time; // e.g. 10:30 AM
    private String mode; // "Online" or "Offline"

    // Parameterized Constructor
    public Interview(String interviewId, String applicationId, String date, String time, String mode) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.date = date;
        this.time = time;
        this.mode = mode;
    }

    // Getters and Setters
    public String getInterviewId() { return interviewId; }
    public void setInterviewId(String interviewId) { this.interviewId = interviewId; }

    public String getApplicationId() { return applicationId; }
    public void setApplicationId(String applicationId) { this.applicationId = applicationId; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    @Override
    public String toString() {
        return interviewId + " (App: " + applicationId + ", Date: " + date + " " + time + ", " + mode + ")";
    }
}

// =========================================================================
// MAIN PUBLIC CLASS & SWING GUI (Modules I to VIII)
// =========================================================================

public class StudentPlacementManagementSystem extends JFrame {

    // ---------------------------------------------------------------------
    // REQUIRED DATA STRUCTURES (Module VII: Collections Framework)
    // ---------------------------------------------------------------------
    // 1. ArrayList for Students
    private ArrayList<Student> studentList = new ArrayList<>();

    // 2. ArrayList for Companies
    private ArrayList<Company> companyList = new ArrayList<>();

    // 3. ArrayList for Jobs
    private ArrayList<Job> jobList = new ArrayList<>();

    // 4. LinkedList for Application History
    private LinkedList<Application> applicationList = new LinkedList<>();

    // 5. LinkedList for Interview Schedules
    private LinkedList<Interview> interviewList = new LinkedList<>();

    // 6. HashMap for Student ID -> Student Lookup (Fast O(1) Search)
    private HashMap<String, Student> studentMap = new HashMap<>();

    // 7. TreeMap for Jobs sorted by Salary/Package
    // Key: Salary (Double), Value: List of Jobs with that package
    private TreeMap<Double, ArrayList<Job>> jobsBySalary = new TreeMap<>(Collections.reverseOrder());

    // ---------------------------------------------------------------------
    // GUI COMPONENTS
    // ---------------------------------------------------------------------
    private JTabbedPane tabbedPane;

    // Student Tab Fields
    private JTextField txtStudentId, txtStudentName, txtStudentCGPA, txtStudentEmail, txtStudentPhone, txtStudentSkills;
    private JTextField txtSearchStudent;
    private JTable studentTable;
    private DefaultTableModel studentTableModel;

    // Company Tab Fields
    private JTextField txtCompanyId, txtCompanyName, txtCompanyLocation, txtCompanyIndustry;
    private JTextField txtSearchCompany;
    private JTable companyTable;
    private DefaultTableModel companyTableModel;

    // Job Tab Fields
    private JTextField txtJobId, txtJobCompanyId, txtJobTitle, txtJobSalary, txtJobMinCGPA, txtJobSkills;
    private JTextField txtSearchJob;
    private JTable jobTable;
    private DefaultTableModel jobTableModel;

    // Application & Eligibility Fields
    private JTextField txtAppId, txtAppStudentId, txtAppJobId;
    private JComboBox<String> comboAppStatus;
    private JTextArea txtEligibilityResult;
    private JTable applicationTable;
    private DefaultTableModel applicationTableModel;

    // Interview Fields
    private JTextField txtInterviewId, txtInterviewAppId, txtInterviewDate, txtInterviewTime;
    private JComboBox<String> comboInterviewMode;
    private JTable interviewTable;
    private DefaultTableModel interviewTableModel;

    // Dashboard & Report Labels
    private JLabel lblStatStudents, lblStatCompanies, lblStatJobs, lblStatApplications, lblStatInterviews, lblStatPlaced;
    private JTable reportPlacedTable;
    private DefaultTableModel reportPlacedTableModel;
    private JTextArea txtReportSummary;

    // Modern College Color Palette
    private static final Color PRIMARY_COLOR = new Color(30, 58, 138);   // Deep Navy Blue
    private static final Color ACCENT_COLOR = new Color(14, 116, 144);   // Ocean Blue
    private static final Color BG_LIGHT = new Color(248, 250, 252);      // Off-white / light slate
    private static final Color CARD_BG = Color.WHITE;
    private static final Color TEXT_DARK = new Color(15, 23, 42);        // Slate dark
    private static final Font TITLE_FONT = new Font("SansSerif", Font.BOLD, 18);
    private static final Font HEADER_FONT = new Font("SansSerif", Font.BOLD, 14);
    private static final Font REGULAR_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Font BOLD_FONT = new Font("SansSerif", Font.BOLD, 12);

    /**
     * Constructor sets up the main application window and components.
     */
    public StudentPlacementManagementSystem() {
        super("Student Placement Management System - Case Study 31 (B.Tech CSE)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null); // Center on screen
        getContentPane().setBackground(BG_LIGHT);

        // Preload initial placement drive sample records
        loadSampleData();

        // Build the GUI layout
        initComponents();

        // Populate tables with initial sample records
        refreshAllTables();
    }

    /**
     * Initialize GUI layout and tabs.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        // Top Banner / Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitle = new JLabel("STUDENT PLACEMENT MANAGEMENT SYSTEM");
        lblTitle.setFont(TITLE_FONT);
        lblTitle.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel("Case Study 31 | Java Programming (B.Tech CSE Semester III)");
        lblSub.setFont(REGULAR_FONT);
        lblSub.setForeground(new Color(224, 231, 255));

        headerPanel.add(lblTitle, BorderLayout.NORTH);
        headerPanel.add(lblSub, BorderLayout.SOUTH);
        add(headerPanel, BorderLayout.NORTH);

        // Tabbed Pane for Navigation
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(HEADER_FONT);

        tabbedPane.addTab("Dashboard", createDashboardPanel());
        tabbedPane.addTab("Students", createStudentPanel());
        tabbedPane.addTab("Companies", createCompanyPanel());
        tabbedPane.addTab("Jobs", createJobPanel());
        tabbedPane.addTab("Applications & Eligibility", createApplicationPanel());
        tabbedPane.addTab("Interviews", createInterviewPanel());
        tabbedPane.addTab("Placement Report", createReportPanel());

        add(tabbedPane, BorderLayout.CENTER);

        // Bottom Status Bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBackground(new Color(241, 245, 249));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(203, 213, 225)));
        JLabel lblStatus = new JLabel("Status: Placement System Active | Academic Year 2026–2027 | ITM Skills University");
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));
        lblStatus.setForeground(new Color(71, 85, 105));
        statusBar.add(lblStatus);
        add(statusBar, BorderLayout.SOUTH);
    }

    // =========================================================================
    // TAB 1: DASHBOARD
    // =========================================================================
    private JPanel createDashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Quick Stats Panel (Cards)
        JPanel statsPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        statsPanel.setOpaque(false);

        lblStatStudents = new JLabel("0", SwingConstants.CENTER);
        lblStatCompanies = new JLabel("0", SwingConstants.CENTER);
        lblStatJobs = new JLabel("0", SwingConstants.CENTER);
        lblStatApplications = new JLabel("0", SwingConstants.CENTER);
        lblStatInterviews = new JLabel("0", SwingConstants.CENTER);
        lblStatPlaced = new JLabel("0", SwingConstants.CENTER);

        statsPanel.add(createCard("Total Students", lblStatStudents, new Color(59, 130, 246)));
        statsPanel.add(createCard("Registered Companies", lblStatCompanies, new Color(16, 185, 129)));
        statsPanel.add(createCard("Job Openings", lblStatJobs, new Color(245, 158, 11)));
        statsPanel.add(createCard("Applications Submitted", lblStatApplications, new Color(139, 92, 246)));
        statsPanel.add(createCard("Interviews Scheduled", lblStatInterviews, new Color(236, 72, 153)));
        statsPanel.add(createCard("Selected Students", lblStatPlaced, new Color(20, 184, 166)));

        panel.add(statsPanel, BorderLayout.NORTH);

        // Information / Guidelines Center Box
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(CARD_BG);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblGuideTitle = new JLabel("Training & Placement Cell - System Guidelines & Operations");
        lblGuideTitle.setFont(HEADER_FONT);
        lblGuideTitle.setForeground(PRIMARY_COLOR);

        JTextArea txtGuide = new JTextArea();
        txtGuide.setFont(REGULAR_FONT);
        txtGuide.setEditable(false);
        txtGuide.setLineWrap(true);
        txtGuide.setWrapStyleWord(true);
        txtGuide.setBackground(CARD_BG);
        txtGuide.setText(
            "Welcome to the Centralized Campus Recruitment & Placement Management System.\n\n" +
            "CORE MODULES & CAPABILITIES:\n" +
            " • Student Profiles: Centralized database maintaining academic records (CGPA) and technical skill proficiencies.\n" +
            " • Corporate Partners: Directory of registered recruitment companies across IT, Product, and Service sectors.\n" +
            " • Job Openings: Active recruitment listings with salary packages, minimum CGPA criteria, and required skill sets.\n" +
            " • Automated Eligibility Verification: Dual-criteria screening validating minimum CGPA and technical skill prerequisites.\n" +
            " • Application Tracking: Lifecycle management of student applications (Applied -> Shortlisted -> Selected / Rejected).\n" +
            " • Interview Management: Scheduling and tracking of online assessments and technical/HR interviews.\n" +
            " • Institutional Placement Reports: Real-time analytics showing placement rates, salary distributions, and selection rosters.\n\n" +
            "OPERATING GUIDELINES FOR PLACEMENT COORDINATORS:\n" +
            " 1. Student Management: Register new candidates and update academic credentials in the 'Students' tab.\n" +
            " 2. Company & Job Management: Maintain corporate partners and publish verified recruitment openings in the 'Jobs' tab.\n" +
            " 3. Eligibility Screening: Verify applicant qualification using the 'Check Eligibility' engine before approving applications.\n" +
            " 4. Shortlisting & Interviewing: Transition qualified candidates to 'Shortlisted' status and coordinate interview schedules.\n" +
            " 5. Placement Confirmation: Record final selections and view institutional placement summaries in the 'Placement Report' tab."
        );

        infoPanel.add(lblGuideTitle, BorderLayout.NORTH);
        infoPanel.add(new JScrollPane(txtGuide), BorderLayout.CENTER);

        panel.add(infoPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createCard(String title, JLabel valueLabel, Color accent) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, accent),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(BOLD_FONT);
        lblTitle.setForeground(new Color(100, 116, 139));

        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        valueLabel.setForeground(TEXT_DARK);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }

    // =========================================================================
    // TAB 2: STUDENT MANAGEMENT (CRUD + SEARCH + SORT)
    // =========================================================================
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel (Left)
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createTitledBorder("Student Details (Array for Skills)"));
        formPanel.setPreferredSize(new Dimension(360, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtStudentId = new JTextField();
        txtStudentName = new JTextField();
        txtStudentCGPA = new JTextField();
        txtStudentEmail = new JTextField();
        txtStudentPhone = new JTextField();
        txtStudentSkills = new JTextField(); // Comma-separated -> String[]

        int row = 0;
        addFormField(formPanel, gbc, "Student ID (e.g. S106):", txtStudentId, row++);
        addFormField(formPanel, gbc, "Full Name:", txtStudentName, row++);
        addFormField(formPanel, gbc, "CGPA (0.0 to 10.0):", txtStudentCGPA, row++);
        addFormField(formPanel, gbc, "Email Address:", txtStudentEmail, row++);
        addFormField(formPanel, gbc, "Phone Number:", txtStudentPhone, row++);
        addFormField(formPanel, gbc, "Skills (Comma separated):", txtStudentSkills, row++);

        // CRUD Buttons Panel
        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 6, 6));
        btnPanel.setOpaque(false);

        JButton btnAdd = createButton("Add Student", PRIMARY_COLOR);
        JButton btnUpdate = createButton("Update", ACCENT_COLOR);
        JButton btnDelete = createButton("Delete", new Color(220, 38, 38));
        JButton btnClear = createButton("Clear Fields", new Color(100, 116, 139));
        JButton btnSortCGPA = createButton("Sort by CGPA", new Color(124, 58, 237));
        JButton btnResetList = createButton("Show All", new Color(71, 85, 105));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnSortCGPA);
        btnPanel.add(btnResetList);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        // Table Panel (Right)
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Student Records (ArrayList + HashMap Lookup)"));

        // Search Bar
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar.setOpaque(false);
        searchBar.add(new JLabel("Search (ID / Name):"));
        txtSearchStudent = new JTextField(15);
        JButton btnSearch = createButton("Search", PRIMARY_COLOR);
        searchBar.add(txtSearchStudent);
        searchBar.add(btnSearch);
        tablePanel.add(searchBar, BorderLayout.NORTH);

        // Student Table
        String[] cols = {"Student ID", "Name", "CGPA", "Email", "Phone", "Skills"};
        studentTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(22);
        tablePanel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        // Click row to populate form
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = studentTable.getSelectedRow();
                if (r != -1) {
                    txtStudentId.setText(studentTableModel.getValueAt(r, 0).toString());
                    txtStudentName.setText(studentTableModel.getValueAt(r, 1).toString());
                    txtStudentCGPA.setText(studentTableModel.getValueAt(r, 2).toString());
                    txtStudentEmail.setText(studentTableModel.getValueAt(r, 3).toString());
                    txtStudentPhone.setText(studentTableModel.getValueAt(r, 4).toString());
                    txtStudentSkills.setText(studentTableModel.getValueAt(r, 5).toString());
                }
            }
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        btnAdd.addActionListener(e -> addStudentAction());
        btnUpdate.addActionListener(e -> updateStudentAction());
        btnDelete.addActionListener(e -> deleteStudentAction());
        btnClear.addActionListener(e -> clearStudentFields());
        btnSearch.addActionListener(e -> searchStudentAction());
        btnSortCGPA.addActionListener(e -> sortStudentsByCGPAAction());
        btnResetList.addActionListener(e -> refreshStudentTable(studentList));

        return panel;
    }

    // =========================================================================
    // TAB 3: COMPANY MANAGEMENT (CRUD + SEARCH)
    // =========================================================================
    private JPanel createCompanyPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createTitledBorder("Company Details"));
        formPanel.setPreferredSize(new Dimension(360, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtCompanyId = new JTextField();
        txtCompanyName = new JTextField();
        txtCompanyLocation = new JTextField();
        txtCompanyIndustry = new JTextField();

        int row = 0;
        addFormField(formPanel, gbc, "Company ID (e.g. C104):", txtCompanyId, row++);
        addFormField(formPanel, gbc, "Company Name:", txtCompanyName, row++);
        addFormField(formPanel, gbc, "Location (City):", txtCompanyLocation, row++);
        addFormField(formPanel, gbc, "Industry / Domain:", txtCompanyIndustry, row++);

        JPanel btnPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        btnPanel.setOpaque(false);

        JButton btnAdd = createButton("Add Company", PRIMARY_COLOR);
        JButton btnUpdate = createButton("Update", ACCENT_COLOR);
        JButton btnDelete = createButton("Delete", new Color(220, 38, 38));
        JButton btnClear = createButton("Clear Fields", new Color(100, 116, 139));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Company Records (ArrayList<Company>)"));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar.setOpaque(false);
        searchBar.add(new JLabel("Search (ID / Name):"));
        txtSearchCompany = new JTextField(15);
        JButton btnSearch = createButton("Search", PRIMARY_COLOR);
        JButton btnReset = createButton("Show All", new Color(71, 85, 105));
        searchBar.add(txtSearchCompany);
        searchBar.add(btnSearch);
        searchBar.add(btnReset);
        tablePanel.add(searchBar, BorderLayout.NORTH);

        String[] cols = {"Company ID", "Company Name", "Location", "Industry"};
        companyTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        companyTable = new JTable(companyTableModel);
        companyTable.setRowHeight(22);
        tablePanel.add(new JScrollPane(companyTable), BorderLayout.CENTER);

        companyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = companyTable.getSelectedRow();
                if (r != -1) {
                    txtCompanyId.setText(companyTableModel.getValueAt(r, 0).toString());
                    txtCompanyName.setText(companyTableModel.getValueAt(r, 1).toString());
                    txtCompanyLocation.setText(companyTableModel.getValueAt(r, 2).toString());
                    txtCompanyIndustry.setText(companyTableModel.getValueAt(r, 3).toString());
                }
            }
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        // Listeners
        btnAdd.addActionListener(e -> addCompanyAction());
        btnUpdate.addActionListener(e -> updateCompanyAction());
        btnDelete.addActionListener(e -> deleteCompanyAction());
        btnClear.addActionListener(e -> clearCompanyFields());
        btnSearch.addActionListener(e -> searchCompanyAction());
        btnReset.addActionListener(e -> refreshCompanyTable(companyList));

        return panel;
    }

    // =========================================================================
    // TAB 4: JOB MANAGEMENT (CRUD + SEARCH + TREEMAP SALARY SORT)
    // =========================================================================
    private JPanel createJobPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createTitledBorder("Job Opening Details"));
        formPanel.setPreferredSize(new Dimension(360, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtJobId = new JTextField();
        txtJobCompanyId = new JTextField();
        txtJobTitle = new JTextField();
        txtJobSalary = new JTextField();
        txtJobMinCGPA = new JTextField();
        txtJobSkills = new JTextField();

        int row = 0;
        addFormField(formPanel, gbc, "Job ID (e.g. J105):", txtJobId, row++);
        addFormField(formPanel, gbc, "Company ID (e.g. C101):", txtJobCompanyId, row++);
        addFormField(formPanel, gbc, "Job Title:", txtJobTitle, row++);
        addFormField(formPanel, gbc, "Salary / Package (LPA):", txtJobSalary, row++);
        addFormField(formPanel, gbc, "Minimum CGPA (0-10):", txtJobMinCGPA, row++);
        addFormField(formPanel, gbc, "Required Skills (Comma sep):", txtJobSkills, row++);

        JPanel btnPanel = new JPanel(new GridLayout(3, 2, 6, 6));
        btnPanel.setOpaque(false);

        JButton btnAdd = createButton("Add Job", PRIMARY_COLOR);
        JButton btnUpdate = createButton("Update", ACCENT_COLOR);
        JButton btnDelete = createButton("Delete", new Color(220, 38, 38));
        JButton btnClear = createButton("Clear Fields", new Color(100, 116, 139));
        JButton btnSortSalary = createButton("Sort by Salary (TreeMap)", new Color(124, 58, 237));
        JButton btnReset = createButton("Show All", new Color(71, 85, 105));

        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnClear);
        btnPanel.add(btnSortSalary);
        btnPanel.add(btnReset);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Job Postings (TreeMap Sorted View Available)"));

        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar.setOpaque(false);
        searchBar.add(new JLabel("Search (ID / Title):"));
        txtSearchJob = new JTextField(15);
        JButton btnSearch = createButton("Search", PRIMARY_COLOR);
        searchBar.add(txtSearchJob);
        searchBar.add(btnSearch);
        tablePanel.add(searchBar, BorderLayout.NORTH);

        String[] cols = {"Job ID", "Company ID", "Job Title", "Salary (LPA)", "Min CGPA", "Required Skills"};
        jobTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        jobTable = new JTable(jobTableModel);
        jobTable.setRowHeight(22);
        tablePanel.add(new JScrollPane(jobTable), BorderLayout.CENTER);

        jobTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = jobTable.getSelectedRow();
                if (r != -1) {
                    txtJobId.setText(jobTableModel.getValueAt(r, 0).toString());
                    txtJobCompanyId.setText(jobTableModel.getValueAt(r, 1).toString());
                    txtJobTitle.setText(jobTableModel.getValueAt(r, 2).toString());
                    txtJobSalary.setText(jobTableModel.getValueAt(r, 3).toString());
                    txtJobMinCGPA.setText(jobTableModel.getValueAt(r, 4).toString());
                    txtJobSkills.setText(jobTableModel.getValueAt(r, 5).toString());
                }
            }
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        // Listeners
        btnAdd.addActionListener(e -> addJobAction());
        btnUpdate.addActionListener(e -> updateJobAction());
        btnDelete.addActionListener(e -> deleteJobAction());
        btnClear.addActionListener(e -> clearJobFields());
        btnSearch.addActionListener(e -> searchJobAction());
        btnSortSalary.addActionListener(e -> sortJobsBySalaryAction());
        btnReset.addActionListener(e -> refreshJobTable(jobList));

        return panel;
    }

    // =========================================================================
    // TAB 5: APPLICATIONS & ELIGIBILITY CHECKING (LinkedList + Exception Handling)
    // =========================================================================
    private JPanel createApplicationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left Panel: Form & Eligibility Engine
        JPanel leftPanel = new JPanel(new BorderLayout(10, 10));
        leftPanel.setPreferredSize(new Dimension(380, 500));
        leftPanel.setOpaque(false);

        // Form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createTitledBorder("Application & Shortlisting Actions"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtAppId = new JTextField();
        txtAppStudentId = new JTextField();
        txtAppJobId = new JTextField();
        String[] statuses = {"Applied", "Shortlisted", "Selected", "Rejected"};
        comboAppStatus = new JComboBox<>(statuses);

        int row = 0;
        addFormField(formPanel, gbc, "Application ID (e.g. APP-104):", txtAppId, row++);
        addFormField(formPanel, gbc, "Student ID (e.g. S101):", txtAppStudentId, row++);
        addFormField(formPanel, gbc, "Job ID (e.g. J101):", txtAppJobId, row++);
        addFormField(formPanel, gbc, "Status:", comboAppStatus, row++);

        JPanel actionBtnPanel = new JPanel(new GridLayout(2, 2, 6, 6));
        actionBtnPanel.setOpaque(false);

        JButton btnCheckElig = createButton("Check Eligibility", new Color(14, 116, 144));
        JButton btnApply = createButton("Apply for Job", PRIMARY_COLOR);
        JButton btnShortlist = createButton("Shortlist Candidate", new Color(16, 185, 129));
        JButton btnUpdateStatus = createButton("Update Status", new Color(245, 158, 11));

        actionBtnPanel.add(btnCheckElig);
        actionBtnPanel.add(btnApply);
        actionBtnPanel.add(btnShortlist);
        actionBtnPanel.add(btnUpdateStatus);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(actionBtnPanel, gbc);

        leftPanel.add(formPanel, BorderLayout.NORTH);

        // Eligibility Evaluation Result Display
        JPanel resultPanel = new JPanel(new BorderLayout(5, 5));
        resultPanel.setBackground(CARD_BG);
        resultPanel.setBorder(BorderFactory.createTitledBorder("Eligibility Verification Output"));

        txtEligibilityResult = new JTextArea(8, 20);
        txtEligibilityResult.setFont(REGULAR_FONT);
        txtEligibilityResult.setEditable(false);
        txtEligibilityResult.setBackground(new Color(248, 250, 252));
        txtEligibilityResult.setText("Select or enter a Student ID and Job ID,\nthen click 'Check Eligibility'.");
        resultPanel.add(new JScrollPane(txtEligibilityResult), BorderLayout.CENTER);

        leftPanel.add(resultPanel, BorderLayout.CENTER);

        panel.add(leftPanel, BorderLayout.WEST);

        // Right Panel: Applications Table (LinkedList<Application>)
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Application History (LinkedList<Application>)"));

        String[] cols = {"Application ID", "Student ID", "Student Name", "Job ID", "Job Title", "Status"};
        applicationTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        applicationTable = new JTable(applicationTableModel);
        applicationTable.setRowHeight(22);
        tablePanel.add(new JScrollPane(applicationTable), BorderLayout.CENTER);

        applicationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = applicationTable.getSelectedRow();
                if (r != -1) {
                    txtAppId.setText(applicationTableModel.getValueAt(r, 0).toString());
                    txtAppStudentId.setText(applicationTableModel.getValueAt(r, 1).toString());
                    txtAppJobId.setText(applicationTableModel.getValueAt(r, 3).toString());
                    comboAppStatus.setSelectedItem(applicationTableModel.getValueAt(r, 5).toString());
                }
            }
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        // Action Listeners
        btnCheckElig.addActionListener(e -> checkEligibilityAction());
        btnApply.addActionListener(e -> applyJobAction());
        btnShortlist.addActionListener(e -> shortlistCandidateAction());
        btnUpdateStatus.addActionListener(e -> updateStatusAction());

        return panel;
    }

    // =========================================================================
    // TAB 6: INTERVIEW SCHEDULING (LinkedList<Interview> + Exception Handling)
    // =========================================================================
    private JPanel createInterviewPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CARD_BG);
        formPanel.setBorder(BorderFactory.createTitledBorder("Schedule Interview (Shortlisted Candidates Only)"));
        formPanel.setPreferredSize(new Dimension(380, 500));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        txtInterviewId = new JTextField();
        txtInterviewAppId = new JTextField();
        txtInterviewDate = new JTextField();
        txtInterviewTime = new JTextField();
        String[] modes = {"Online", "Offline"};
        comboInterviewMode = new JComboBox<>(modes);

        int row = 0;
        addFormField(formPanel, gbc, "Interview ID (e.g. INT-103):", txtInterviewId, row++);
        addFormField(formPanel, gbc, "Application ID (e.g. APP-101):", txtInterviewAppId, row++);
        addFormField(formPanel, gbc, "Interview Date (YYYY-MM-DD):", txtInterviewDate, row++);
        addFormField(formPanel, gbc, "Interview Time (e.g. 11:00 AM):", txtInterviewTime, row++);
        addFormField(formPanel, gbc, "Interview Mode:", comboInterviewMode, row++);

        JPanel btnPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        btnPanel.setOpaque(false);

        JButton btnSchedule = createButton("Schedule Interview", PRIMARY_COLOR);
        JButton btnClear = createButton("Clear Fields", new Color(100, 116, 139));

        btnPanel.add(btnSchedule);
        btnPanel.add(btnClear);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        formPanel.add(btnPanel, gbc);

        panel.add(formPanel, BorderLayout.WEST);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout(5, 5));
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Interview Schedules (LinkedList<Interview>)"));

        String[] cols = {"Interview ID", "Application ID", "Student Name", "Job Title", "Date", "Time", "Mode"};
        interviewTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        interviewTable = new JTable(interviewTableModel);
        interviewTable.setRowHeight(22);
        tablePanel.add(new JScrollPane(interviewTable), BorderLayout.CENTER);

        interviewTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int r = interviewTable.getSelectedRow();
                if (r != -1) {
                    txtInterviewId.setText(interviewTableModel.getValueAt(r, 0).toString());
                    txtInterviewAppId.setText(interviewTableModel.getValueAt(r, 1).toString());
                    txtInterviewDate.setText(interviewTableModel.getValueAt(r, 4).toString());
                    txtInterviewTime.setText(interviewTableModel.getValueAt(r, 5).toString());
                    comboInterviewMode.setSelectedItem(interviewTableModel.getValueAt(r, 6).toString());
                }
            }
        });

        panel.add(tablePanel, BorderLayout.CENTER);

        // Listeners
        btnSchedule.addActionListener(e -> scheduleInterviewAction());
        btnClear.addActionListener(e -> clearInterviewFields());

        return panel;
    }

    // =========================================================================
    // TAB 7: PLACEMENT REPORT & STATISTICS
    // =========================================================================
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_LIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Top Summary Bar
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(CARD_BG);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240)),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        JLabel lblRepTitle = new JLabel("Official University Placement Report & Results");
        lblRepTitle.setFont(TITLE_FONT);
        lblRepTitle.setForeground(PRIMARY_COLOR);

        JButton btnRefresh = createButton("Refresh Report Data", PRIMARY_COLOR);
        btnRefresh.addActionListener(e -> refreshReportData());

        topBar.add(lblRepTitle, BorderLayout.WEST);
        topBar.add(btnRefresh, BorderLayout.EAST);
        panel.add(topBar, BorderLayout.NORTH);

        // Split Panel: Summary Text on Left, Selected Students Table on Right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(380);

        // Summary Text Area
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBackground(CARD_BG);
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Placement Cell Statistics"));

        txtReportSummary = new JTextArea();
        txtReportSummary.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtReportSummary.setEditable(false);
        txtReportSummary.setBackground(new Color(248, 250, 252));
        summaryPanel.add(new JScrollPane(txtReportSummary), BorderLayout.CENTER);
        splitPane.setLeftComponent(summaryPanel);

        // Selected Students Table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(CARD_BG);
        tablePanel.setBorder(BorderFactory.createTitledBorder("Successfully Placed / Selected Students"));

        String[] cols = {"Student ID", "Student Name", "Company", "Job Title", "Package (LPA)", "Status"};
        reportPlacedTableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        reportPlacedTable = new JTable(reportPlacedTableModel);
        reportPlacedTable.setRowHeight(22);
        tablePanel.add(new JScrollPane(reportPlacedTable), BorderLayout.CENTER);
        splitPane.setRightComponent(tablePanel);

        panel.add(splitPane, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================================
    // CRUD IMPLEMENTATIONS: STUDENT MODULE
    // =========================================================================

    /**
     * Creates and adds a new student after thorough validation.
     */
    private void addStudentAction() {
        try {
            String id = txtStudentId.getText().trim();
            String name = txtStudentName.getText().trim();
            String cgpaStr = txtStudentCGPA.getText().trim();
            String email = txtStudentEmail.getText().trim();
            String phone = txtStudentPhone.getText().trim();
            String skillsStr = txtStudentSkills.getText().trim();

            // Validation (Module II & VI)
            if (id.isEmpty() || name.isEmpty() || cgpaStr.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Error: All fields are mandatory!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check duplicate ID using HashMap (O(1) search)
            if (studentMap.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "Validation Error: Student ID '" + id + "' already exists!", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double cgpa = Double.parseDouble(cgpaStr);
            if (cgpa < 0.0 || cgpa > 10.0) {
                JOptionPane.showMessageDialog(this, "Validation Error: CGPA must be between 0.0 and 10.0!", "Invalid CGPA", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse skills into Array (Case Study Requirement: Array for skills)
            String[] skills = parseArray(skillsStr);

            Student s = new Student(id, name, cgpa, email, phone, skills);

            // Add to ArrayList and HashMap
            studentList.add(s);
            studentMap.put(id, s);

            refreshStudentTable(studentList);
            refreshDashboardStats();
            clearStudentFields();

            JOptionPane.showMessageDialog(this, "Student added successfully!\nStored in ArrayList & HashMap.", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Please enter a valid decimal number for CGPA.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates existing student details.
     */
    private void updateStudentAction() {
        try {
            String id = txtStudentId.getText().trim();
            if (id.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please select or enter the Student ID to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Student s = studentMap.get(id);
            if (s == null) {
                JOptionPane.showMessageDialog(this, "Student ID '" + id + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String name = txtStudentName.getText().trim();
            String cgpaStr = txtStudentCGPA.getText().trim();
            String email = txtStudentEmail.getText().trim();
            String phone = txtStudentPhone.getText().trim();
            String skillsStr = txtStudentSkills.getText().trim();

            if (name.isEmpty() || cgpaStr.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Error: Fields cannot be empty during update!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double cgpa = Double.parseDouble(cgpaStr);
            if (cgpa < 0.0 || cgpa > 10.0) {
                JOptionPane.showMessageDialog(this, "Validation Error: CGPA must be between 0.0 and 10.0!", "Invalid CGPA", JOptionPane.ERROR_MESSAGE);
                return;
            }

            s.setName(name);
            s.setCgpa(cgpa);
            s.setEmail(email);
            s.setPhone(phone);
            s.setSkills(parseArray(skillsStr));

            refreshStudentTable(studentList);
            JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Invalid number for CGPA.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Deletes student from both ArrayList and HashMap.
     */
    private void deleteStudentAction() {
        String id = txtStudentId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter the Student ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Student s = studentMap.get(id);
        if (s == null) {
            JOptionPane.showMessageDialog(this, "Student ID '" + id + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete student: " + s.getName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            studentList.remove(s);
            studentMap.remove(id);
            refreshStudentTable(studentList);
            refreshDashboardStats();
            clearStudentFields();
            JOptionPane.showMessageDialog(this, "Student deleted successfully from ArrayList and HashMap.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Searches students by ID (using HashMap) or Name (using loop).
     */
    private void searchStudentAction() {
        String query = txtSearchStudent.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            refreshStudentTable(studentList);
            return;
        }

        ArrayList<Student> results = new ArrayList<>();

        // Fast check by ID using HashMap
        Student directMatch = studentMap.get(query.toUpperCase());
        if (directMatch != null) {
            results.add(directMatch);
        } else {
            // Linear search by name or ID (Module II: Loops & Conditionals)
            for (Student s : studentList) {
                if (s.getStudentId().toLowerCase().contains(query) || s.getName().toLowerCase().contains(query)) {
                    results.add(s);
                }
            }
        }

        refreshStudentTable(results);
        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No student found matching: " + query, "Search Result", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Sorts students by CGPA in descending order (Highest first).
     * Uses a simple, explainable Bubble Sort algorithm (Module III: Array Applications).
     */
    private void sortStudentsByCGPAAction() {
        if (studentList.size() <= 1) return;

        // Create a copy list to sort
        ArrayList<Student> sorted = new ArrayList<>(studentList);

        // Bubble Sort: Compares adjacent student CGPA and sorts in descending order
        int n = sorted.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sorted.get(j).getCgpa() < sorted.get(j + 1).getCgpa()) {
                    // Swap
                    Student temp = sorted.get(j);
                    sorted.set(j, sorted.get(j + 1));
                    sorted.set(j + 1, temp);
                }
            }
        }

        refreshStudentTable(sorted);
        JOptionPane.showMessageDialog(this, "Students sorted by CGPA (Descending) using Bubble Sort.", "Sort Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void clearStudentFields() {
        txtStudentId.setText("");
        txtStudentName.setText("");
        txtStudentCGPA.setText("");
        txtStudentEmail.setText("");
        txtStudentPhone.setText("");
        txtStudentSkills.setText("");
        txtSearchStudent.setText("");
    }

    private void refreshStudentTable(ArrayList<Student> list) {
        studentTableModel.setRowCount(0);
        for (Student s : list) {
            studentTableModel.addRow(new Object[]{
                s.getStudentId(),
                s.getName(),
                String.format("%.2f", s.getCgpa()),
                s.getEmail(),
                s.getPhone(),
                s.getSkillsFormatted()
            });
        }
    }

    // =========================================================================
    // CRUD IMPLEMENTATIONS: COMPANY MODULE
    // =========================================================================

    private void addCompanyAction() {
        String id = txtCompanyId.getText().trim();
        String name = txtCompanyName.getText().trim();
        String location = txtCompanyLocation.getText().trim();
        String industry = txtCompanyIndustry.getText().trim();

        if (id.isEmpty() || name.isEmpty() || location.isEmpty() || industry.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Validation Error: All company fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check duplicate company ID
        for (Company c : companyList) {
            if (c.getCompanyId().equalsIgnoreCase(id)) {
                JOptionPane.showMessageDialog(this, "Validation Error: Company ID '" + id + "' already exists!", "Duplicate ID", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        Company c = new Company(id, name, location, industry);
        companyList.add(c);
        refreshCompanyTable(companyList);
        refreshDashboardStats();
        clearCompanyFields();
        JOptionPane.showMessageDialog(this, "Company registered successfully in ArrayList<Company>!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void updateCompanyAction() {
        String id = txtCompanyId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter the Company ID to update.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Company found = findCompany(id);
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Company ID '" + id + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = txtCompanyName.getText().trim();
        String location = txtCompanyLocation.getText().trim();
        String industry = txtCompanyIndustry.getText().trim();

        if (name.isEmpty() || location.isEmpty() || industry.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Validation Error: Fields cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        found.setCompanyName(name);
        found.setLocation(location);
        found.setIndustry(industry);

        refreshCompanyTable(companyList);
        JOptionPane.showMessageDialog(this, "Company details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void deleteCompanyAction() {
        String id = txtCompanyId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter Company ID to delete.", "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Company found = findCompany(id);
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Company ID '" + id + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete company: " + found.getCompanyName() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            companyList.remove(found);
            refreshCompanyTable(companyList);
            refreshDashboardStats();
            clearCompanyFields();
            JOptionPane.showMessageDialog(this, "Company deleted successfully.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchCompanyAction() {
        String q = txtSearchCompany.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            refreshCompanyTable(companyList);
            return;
        }

        ArrayList<Company> results = new ArrayList<>();
        for (Company c : companyList) {
            if (c.getCompanyId().toLowerCase().contains(q) || c.getCompanyName().toLowerCase().contains(q) || c.getIndustry().toLowerCase().contains(q)) {
                results.add(c);
            }
        }
        refreshCompanyTable(results);
    }

    private void clearCompanyFields() {
        txtCompanyId.setText("");
        txtCompanyName.setText("");
        txtCompanyLocation.setText("");
        txtCompanyIndustry.setText("");
        txtSearchCompany.setText("");
    }

    private void refreshCompanyTable(ArrayList<Company> list) {
        companyTableModel.setRowCount(0);
        for (Company c : list) {
            companyTableModel.addRow(new Object[]{
                c.getCompanyId(),
                c.getCompanyName(),
                c.getLocation(),
                c.getIndustry()
            });
        }
    }

    // =========================================================================
    // CRUD IMPLEMENTATIONS: JOB MODULE (TREEMAP INTEGRATION)
    // =========================================================================

    private void addJobAction() {
        try {
            String id = txtJobId.getText().trim();
            String compId = txtJobCompanyId.getText().trim();
            String title = txtJobTitle.getText().trim();
            String salStr = txtJobSalary.getText().trim();
            String minCgpaStr = txtJobMinCGPA.getText().trim();
            String skillsStr = txtJobSkills.getText().trim();

            if (id.isEmpty() || compId.isEmpty() || title.isEmpty() || salStr.isEmpty() || minCgpaStr.isEmpty() || skillsStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Validation Error: All job fields are required!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Check duplicate job ID
            for (Job j : jobList) {
                if (j.getJobId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(this, "Validation Error: Job ID '" + id + "' already exists!", "Duplicate Job ID", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

            // Check company exists
            if (findCompany(compId) == null) {
                JOptionPane.showMessageDialog(this, "Validation Error: Company ID '" + compId + "' does not exist in registered companies!", "Company Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salary = Double.parseDouble(salStr);
            if (salary <= 0) {
                JOptionPane.showMessageDialog(this, "Validation Error: Salary package must be positive!", "Invalid Salary", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double minCgpa = Double.parseDouble(minCgpaStr);
            if (minCgpa < 0 || minCgpa > 10) {
                JOptionPane.showMessageDialog(this, "Validation Error: Minimum CGPA must be between 0.0 and 10.0!", "Invalid CGPA", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] reqSkills = parseArray(skillsStr);

            Job job = new Job(id, compId, title, salary, minCgpa, reqSkills);
            jobList.add(job);

            // Update TreeMap (Case Study Requirement: TreeMap Jobs sorted by salary/package)
            rebuildJobTreeMap();

            refreshJobTable(jobList);
            refreshDashboardStats();
            clearJobFields();
            JOptionPane.showMessageDialog(this, "Job opening added successfully and indexed in TreeMap by salary!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Please enter valid numbers for Salary and Min CGPA.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateJobAction() {
        try {
            String id = txtJobId.getText().trim();
            Job found = findJob(id);
            if (found == null) {
                JOptionPane.showMessageDialog(this, "Job ID '" + id + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String compId = txtJobCompanyId.getText().trim();
            if (findCompany(compId) == null) {
                JOptionPane.showMessageDialog(this, "Validation Error: Company ID '" + compId + "' does not exist!", "Invalid Company", JOptionPane.ERROR_MESSAGE);
                return;
            }

            found.setCompanyId(compId);
            found.setJobTitle(txtJobTitle.getText().trim());
            found.setSalary(Double.parseDouble(txtJobSalary.getText().trim()));
            found.setMinimumCGPA(Double.parseDouble(txtJobMinCGPA.getText().trim()));
            found.setRequiredSkills(parseArray(txtJobSkills.getText().trim()));

            rebuildJobTreeMap();
            refreshJobTable(jobList);
            JOptionPane.showMessageDialog(this, "Job opening updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Validation Error: Invalid number entered.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteJobAction() {
        String id = txtJobId.getText().trim();
        Job found = findJob(id);
        if (found == null) {
            JOptionPane.showMessageDialog(this, "Job ID '" + id + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete Job opening: " + found.getJobTitle() + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            jobList.remove(found);
            rebuildJobTreeMap();
            refreshJobTable(jobList);
            refreshDashboardStats();
            clearJobFields();
            JOptionPane.showMessageDialog(this, "Job opening deleted.", "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void searchJobAction() {
        String q = txtSearchJob.getText().trim().toLowerCase();
        if (q.isEmpty()) {
            refreshJobTable(jobList);
            return;
        }

        ArrayList<Job> results = new ArrayList<>();
        for (Job j : jobList) {
            if (j.getJobId().toLowerCase().contains(q) || j.getJobTitle().toLowerCase().contains(q) || j.getCompanyId().toLowerCase().contains(q)) {
                results.add(j);
            }
        }
        refreshJobTable(results);
    }

    /**
     * Demonstrates TreeMap: Displays all jobs sorted by salary package in descending order.
     */
    private void sortJobsBySalaryAction() {
        rebuildJobTreeMap();
        ArrayList<Job> sortedFromTreeMap = new ArrayList<>();

        // Iterate through TreeMap entries: naturally sorted by salary
        for (Map.Entry<Double, ArrayList<Job>> entry : jobsBySalary.entrySet()) {
            sortedFromTreeMap.addAll(entry.getValue());
        }

        refreshJobTable(sortedFromTreeMap);
        JOptionPane.showMessageDialog(this, "Jobs sorted by Salary/Package using TreeMap (Descending Order).", "TreeMap Sort Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Rebuilds the TreeMap data structure mapping Salary -> List of Jobs.
     */
    private void rebuildJobTreeMap() {
        jobsBySalary.clear();
        for (Job j : jobList) {
            double sal = j.getSalary();
            if (!jobsBySalary.containsKey(sal)) {
                jobsBySalary.put(sal, new ArrayList<>());
            }
            jobsBySalary.get(sal).add(j);
        }
    }

    private void clearJobFields() {
        txtJobId.setText("");
        txtJobCompanyId.setText("");
        txtJobTitle.setText("");
        txtJobSalary.setText("");
        txtJobMinCGPA.setText("");
        txtJobSkills.setText("");
        txtSearchJob.setText("");
    }

    private void refreshJobTable(ArrayList<Job> list) {
        jobTableModel.setRowCount(0);
        for (Job j : list) {
            jobTableModel.addRow(new Object[]{
                j.getJobId(),
                j.getCompanyId(),
                j.getJobTitle(),
                String.format("%.2f", j.getSalary()),
                String.format("%.2f", j.getMinimumCGPA()),
                j.getRequiredSkillsFormatted()
            });
        }
    }

    // =========================================================================
    // ELIGIBILITY CHECKING MODULE (Module III: Array Applications)
    // =========================================================================

    /**
     * Checks if a student meets all eligibility criteria for a job.
     * Criteria:
     *  1. student.cgpa >= job.minimumCGPA
     *  2. student has ALL skills listed in job.requiredSkills[]
     */
    public boolean isStudentEligible(Student student, Job job) {
        if (student == null || job == null) return false;

        // Condition 1: CGPA check
        if (student.getCgpa() < job.getMinimumCGPA()) {
            return false;
        }

        // Condition 2: Skill matching (Array traversal)
        String[] required = job.getRequiredSkills();
        if (required != null) {
            for (int i = 0; i < required.length; i++) {
                if (!student.hasSkill(required[i])) {
                    return false; // Missing at least one required skill
                }
            }
        }

        return true;
    }

    /**
     * Action handler to check eligibility and generate a detailed report in GUI.
     */
    private void checkEligibilityAction() {
        String studentId = txtAppStudentId.getText().trim();
        String jobId = txtAppJobId.getText().trim();

        if (studentId.isEmpty() || jobId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both Student ID and Job ID to evaluate eligibility.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Student student = studentMap.get(studentId);
        Job job = findJob(jobId);

        if (student == null) {
            txtEligibilityResult.setText("ERROR: Student ID '" + studentId + "' not found in registered database!");
            return;
        }

        if (job == null) {
            txtEligibilityResult.setText("ERROR: Job ID '" + jobId + "' not found in active job openings!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== ELIGIBILITY ASSESSMENT ===\n");
        sb.append("Candidate: ").append(student.getName()).append(" (ID: ").append(student.getStudentId()).append(")\n");
        sb.append("Job Title: ").append(job.getJobTitle()).append(" (ID: ").append(job.getJobId()).append(")\n\n");

        // Step 1: CGPA Check
        sb.append("1. CGPA Criterion:\n");
        sb.append("   • Candidate CGPA: ").append(student.getCgpa()).append("\n");
        sb.append("   • Minimum Required: ").append(job.getMinimumCGPA()).append("\n");
        boolean cgpaPass = student.getCgpa() >= job.getMinimumCGPA();
        sb.append("   • Result: ").append(cgpaPass ? "PASSED [✓]" : "FAILED [X]").append("\n\n");

        // Step 2: Skill Criteria Check
        sb.append("2. Skills Criterion (Array Matching):\n");
        sb.append("   • Candidate Skills: ").append(student.getSkillsFormatted()).append("\n");
        sb.append("   • Job Required Skills: ").append(job.getRequiredSkillsFormatted()).append("\n");

        String[] req = job.getRequiredSkills();
        boolean skillsPass = true;
        for (int i = 0; i < req.length; i++) {
            boolean has = student.hasSkill(req[i]);
            sb.append("   - ").append(req[i]).append(": ").append(has ? "Matched [✓]" : "MISSING [X]").append("\n");
            if (!has) skillsPass = false;
        }

        sb.append("\n==============================\n");
        if (cgpaPass && skillsPass) {
            sb.append("OVERALL STATUS: ELIGIBLE FOR APPLICATION [✓]\n");
            sb.append("Candidate satisfies all academic & technical criteria.");
        } else {
            sb.append("OVERALL STATUS: NOT ELIGIBLE [X]\n");
            sb.append("Reason: ");
            if (!cgpaPass) sb.append("CGPA below threshold. ");
            if (!skillsPass) sb.append("Lacks required technical skills. ");
        }

        txtEligibilityResult.setText(sb.toString());
    }

    // =========================================================================
    // APPLICATION MANAGEMENT MODULE (LinkedList + Exception Handling)
    // =========================================================================

    /**
     * Applies for a job. Throws InvalidApplicationException if ineligible or invalid.
     */
    private void applyJobAction() {
        try {
            String appId = txtAppId.getText().trim();
            String studentId = txtAppStudentId.getText().trim();
            String jobId = txtAppJobId.getText().trim();

            if (appId.isEmpty() || studentId.isEmpty() || jobId.isEmpty()) {
                throw new InvalidApplicationException("All fields (Application ID, Student ID, Job ID) are required!");
            }

            // Check duplicate Application ID
            for (Application a : applicationList) {
                if (a.getApplicationId().equalsIgnoreCase(appId)) {
                    throw new InvalidApplicationException("Application ID '" + appId + "' already exists!");
                }
            }

            Student student = studentMap.get(studentId);
            if (student == null) {
                throw new InvalidApplicationException("Student ID '" + studentId + "' does not exist!");
            }

            Job job = findJob(jobId);
            if (job == null) {
                throw new InvalidApplicationException("Job ID '" + jobId + "' does not exist!");
            }

            // Check if student already applied to this specific job
            for (Application a : applicationList) {
                if (a.getStudentId().equalsIgnoreCase(studentId) && a.getJobId().equalsIgnoreCase(jobId)) {
                    throw new InvalidApplicationException("Student " + student.getName() + " has ALREADY applied for Job " + jobId + "!");
                }
            }

            // Strict Eligibility Check (Case Study Requirement: Throw/display if not eligible)
            if (!isStudentEligible(student, job)) {
                throw new InvalidApplicationException("Application Rejected: Student " + student.getName() + " is NOT eligible for this job!");
            }

            // Add Application to LinkedList
            Application newApp = new Application(appId, studentId, jobId, "Applied");
            applicationList.add(newApp);

            refreshApplicationTable();
            refreshDashboardStats();

            JOptionPane.showMessageDialog(this,
                "Application submitted successfully!\n" +
                "Application ID: " + appId + "\n" +
                "Status: Applied\n" +
                "Stored in: LinkedList<Application>",
                "Application Success", JOptionPane.INFORMATION_MESSAGE);

        } catch (InvalidApplicationException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Application Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shortlists candidate: changes status from 'Applied' to 'Shortlisted'.
     */
    private void shortlistCandidateAction() {
        int row = applicationTable.getSelectedRow();
        String appId = txtAppId.getText().trim();

        if (appId.isEmpty() && row != -1) {
            appId = applicationTableModel.getValueAt(row, 0).toString();
        }

        if (appId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an application to shortlist.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Application app = findApplication(appId);
        if (app == null) {
            JOptionPane.showMessageDialog(this, "Application '" + appId + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (app.getStatus().equalsIgnoreCase("Shortlisted")) {
            JOptionPane.showMessageDialog(this, "Application '" + appId + "' is already Shortlisted!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        app.setStatus("Shortlisted");
        comboAppStatus.setSelectedItem("Shortlisted");
        refreshApplicationTable();
        refreshDashboardStats();

        JOptionPane.showMessageDialog(this, "Candidate successfully shortlisted!\nStatus updated to: Shortlisted", "Shortlisted", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Updates status directly (Applied, Shortlisted, Selected, Rejected).
     */
    private void updateStatusAction() {
        String appId = txtAppId.getText().trim();
        if (appId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select or enter the Application ID to update status.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Application app = findApplication(appId);
        if (app == null) {
            JOptionPane.showMessageDialog(this, "Application ID '" + appId + "' not found.", "Not Found", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String newStatus = comboAppStatus.getSelectedItem().toString();
        app.setStatus(newStatus);
        refreshApplicationTable();
        refreshDashboardStats();
        refreshReportData();

        JOptionPane.showMessageDialog(this, "Application " + appId + " status changed to: " + newStatus, "Status Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshApplicationTable() {
        applicationTableModel.setRowCount(0);
        for (Application a : applicationList) {
            Student s = studentMap.get(a.getStudentId());
            Job j = findJob(a.getJobId());

            String studentName = (s != null) ? s.getName() : "Unknown";
            String jobTitle = (j != null) ? j.getJobTitle() : "Unknown";

            applicationTableModel.addRow(new Object[]{
                a.getApplicationId(),
                a.getStudentId(),
                studentName,
                a.getJobId(),
                jobTitle,
                a.getStatus()
            });
        }
    }

    // =========================================================================
    // INTERVIEW SCHEDULING MODULE (LinkedList<Interview> + Exception Handling)
    // =========================================================================

    /**
     * Schedules an interview. Throws InvalidInterviewException if application
     * does not exist, is not shortlisted, or fields are empty.
     */
    private void scheduleInterviewAction() {
        try {
            String intId = txtInterviewId.getText().trim();
            String appId = txtInterviewAppId.getText().trim();
            String date = txtInterviewDate.getText().trim();
            String time = txtInterviewTime.getText().trim();
            String mode = comboInterviewMode.getSelectedItem().toString();

            if (intId.isEmpty() || appId.isEmpty() || date.isEmpty() || time.isEmpty()) {
                throw new InvalidInterviewException("Validation Error: All interview scheduling fields are mandatory!");
            }

            // Check duplicate interview ID
            for (Interview inv : interviewList) {
                if (inv.getInterviewId().equalsIgnoreCase(intId)) {
                    throw new InvalidInterviewException("Interview ID '" + intId + "' already exists!");
                }
            }

            // Check application exists
            Application app = findApplication(appId);
            if (app == null) {
                throw new InvalidInterviewException("Application ID '" + appId + "' does not exist!");
            }

            // Case Study Rule: Application MUST be shortlisted before scheduling
            if (!app.getStatus().equalsIgnoreCase("Shortlisted")) {
                throw new InvalidInterviewException("Scheduling Error: Application status is '" + app.getStatus() +
                        "'. Candidate must be 'Shortlisted' before an interview can be scheduled!");
            }

            // Create Interview and add to LinkedList
            Interview interview = new Interview(intId, appId, date, time, mode);
            interviewList.add(interview);

            refreshInterviewTable();
            refreshDashboardStats();
            clearInterviewFields();

            JOptionPane.showMessageDialog(this,
                "Interview scheduled successfully!\n" +
                "Interview ID: " + intId + "\n" +
                "Application: " + appId + "\n" +
                "Date & Time: " + date + " at " + time + "\n" +
                "Mode: " + mode + "\n" +
                "Stored in: LinkedList<Interview>",
                "Interview Scheduled", JOptionPane.INFORMATION_MESSAGE);

        } catch (InvalidInterviewException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Interview Scheduling Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInterviewFields() {
        txtInterviewId.setText("");
        txtInterviewAppId.setText("");
        txtInterviewDate.setText("");
        txtInterviewTime.setText("");
    }

    private void refreshInterviewTable() {
        interviewTableModel.setRowCount(0);
        for (Interview inv : interviewList) {
            Application app = findApplication(inv.getApplicationId());
            String studentName = "Unknown";
            String jobTitle = "Unknown";
            if (app != null) {
                Student s = studentMap.get(app.getStudentId());
                if (s != null) studentName = s.getName();
                Job j = findJob(app.getJobId());
                if (j != null) jobTitle = j.getJobTitle();
            }

            interviewTableModel.addRow(new Object[]{
                inv.getInterviewId(),
                inv.getApplicationId(),
                studentName,
                jobTitle,
                inv.getDate(),
                inv.getTime(),
                inv.getMode()
            });
        }
    }

    // =========================================================================
    // PLACEMENT REPORT GENERATION MODULE
    // =========================================================================

    private void refreshReportData() {
        reportPlacedTableModel.setRowCount(0);

        int totalStudents = studentList.size();
        int totalCompanies = companyList.size();
        int totalJobs = jobList.size();
        int totalApps = applicationList.size();
        int totalInterviews = interviewList.size();

        int countApplied = 0;
        int countShortlisted = 0;
        int countSelected = 0;
        int countRejected = 0;

        double totalPlacedPackage = 0.0;
        double maxPlacedPackage = 0.0;

        for (Application a : applicationList) {
            String st = a.getStatus();
            if (st.equalsIgnoreCase("Applied")) countApplied++;
            else if (st.equalsIgnoreCase("Shortlisted")) countShortlisted++;
            else if (st.equalsIgnoreCase("Selected")) {
                countSelected++;

                Student s = studentMap.get(a.getStudentId());
                Job j = findJob(a.getJobId());
                Company c = (j != null) ? findCompany(j.getCompanyId()) : null;

                String sName = (s != null) ? s.getName() : "Unknown";
                String cName = (c != null) ? c.getCompanyName() : "Unknown";
                String jTitle = (j != null) ? j.getJobTitle() : "Unknown";
                double pkg = (j != null) ? j.getSalary() : 0.0;

                totalPlacedPackage += pkg;
                if (pkg > maxPlacedPackage) maxPlacedPackage = pkg;

                reportPlacedTableModel.addRow(new Object[]{
                    a.getStudentId(),
                    sName,
                    cName,
                    jTitle,
                    String.format("%.2f LPA", pkg),
                    "Selected / Placed"
                });
            } else if (st.equalsIgnoreCase("Rejected")) {
                countRejected++;
            }
        }

        double avgPackage = (countSelected > 0) ? (totalPlacedPackage / countSelected) : 0.0;
        double placementPercentage = (totalStudents > 0) ? (((double) countSelected / totalStudents) * 100.0) : 0.0;

        StringBuilder sb = new StringBuilder();
        sb.append("============================================================\n");
        sb.append("           UNIVERSITY PLACEMENT CELL SUMMARY REPORT         \n");
        sb.append("============================================================\n\n");
        sb.append(String.format("Total Registered Students:      %d\n", totalStudents));
        sb.append(String.format("Participating Companies:        %d\n", totalCompanies));
        sb.append(String.format("Total Job Openings:             %d\n", totalJobs));
        sb.append(String.format("Total Applications Submitted:   %d\n", totalApps));
        sb.append(String.format("Total Interviews Conducted:     %d\n\n", totalInterviews));
        sb.append("APPLICATION FUNNEL BREAKDOWN:\n");
        sb.append(String.format(" • Applied (Pending Review):    %d\n", countApplied));
        sb.append(String.format(" • Shortlisted Candidates:      %d\n", countShortlisted));
        sb.append(String.format(" • Successfully Selected:       %d\n", countSelected));
        sb.append(String.format(" • Rejected Candidates:         %d\n\n", countRejected));
        sb.append("SALARY / PACKAGE STATISTICS:\n");
        sb.append(String.format(" • Placement Rate:              %.1f%%\n", placementPercentage));
        sb.append(String.format(" • Highest Package Offered:     %.2f LPA\n", maxPlacedPackage));
        sb.append(String.format(" • Average Selected Package:    %.2f LPA\n", avgPackage));
        sb.append("============================================================\n");
        sb.append("Generated by: StudentPlacementManagementSystem (Java Swing)\n");

        txtReportSummary.setText(sb.toString());
    }

    private void refreshDashboardStats() {
        lblStatStudents.setText(String.valueOf(studentList.size()));
        lblStatCompanies.setText(String.valueOf(companyList.size()));
        lblStatJobs.setText(String.valueOf(jobList.size()));
        lblStatApplications.setText(String.valueOf(applicationList.size()));
        lblStatInterviews.setText(String.valueOf(interviewList.size()));

        int placed = 0;
        for (Application a : applicationList) {
            if ("Selected".equalsIgnoreCase(a.getStatus())) placed++;
        }
        lblStatPlaced.setText(String.valueOf(placed));
    }

    private void refreshAllTables() {
        refreshStudentTable(studentList);
        refreshCompanyTable(companyList);
        refreshJobTable(jobList);
        refreshApplicationTable();
        refreshInterviewTable();
        refreshDashboardStats();
        refreshReportData();
    }

    // =========================================================================
    // HELPER LOOKUP & UTILITY METHODS
    // =========================================================================

    private Company findCompany(String compId) {
        for (Company c : companyList) {
            if (c.getCompanyId().equalsIgnoreCase(compId)) return c;
        }
        return null;
    }

    private Job findJob(String jobId) {
        for (Job j : jobList) {
            if (j.getJobId().equalsIgnoreCase(jobId)) return j;
        }
        return null;
    }

    private Application findApplication(String appId) {
        for (Application a : applicationList) {
            if (a.getApplicationId().equalsIgnoreCase(appId)) return a;
        }
        return null;
    }

    /**
     * Splits comma-separated string into a clean String array (Module III).
     */
    private String[] parseArray(String input) {
        if (input == null || input.trim().isEmpty()) return new String[0];
        String[] parts = input.split(",");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = parts[i].trim();
        }
        return parts;
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(BOLD_FONT);
        panel.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.7;
        field.setFont(REGULAR_FONT);
        panel.add(field, gbc);
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(BOLD_FONT);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        return btn;
    }

    // =========================================================================
    // SAMPLE DATA GENERATION (Module VII & Part 32)
    // =========================================================================

    /**
     * Pre-populates realistic records for initial placement cell setup.
     */
    private void loadSampleData() {
        // 1. Sample Students (ArrayList + HashMap)
        Student s1 = new Student("S101", "Aarav Sharma", 8.5, "aarav@univ.edu", "9876543210", new String[]{"Java", "SQL", "Python"});
        Student s2 = new Student("S102", "Riya Patel", 7.8, "riya@univ.edu", "9876543211", new String[]{"Java", "SQL", "Communication"});
        Student s3 = new Student("S103", "Karan Shah", 9.1, "karan@univ.edu", "9876543212", new String[]{"Java", "Spring", "React", "SQL"});
        Student s4 = new Student("S104", "Sneha Joshi", 6.9, "sneha@univ.edu", "9876543213", new String[]{"C++", "HTML", "CSS"});
        Student s5 = new Student("S105", "Aditya Verma", 8.2, "aditya@univ.edu", "9876543214", new String[]{"Java", "Python", "Data Structures"});

        studentList.add(s1);
        studentList.add(s2);
        studentList.add(s3);
        studentList.add(s4);
        studentList.add(s5);

        for (Student s : studentList) {
            studentMap.put(s.getStudentId(), s);
        }

        // 2. Sample Companies (ArrayList)
        Company c1 = new Company("C101", "TechNova Solutions", "Mumbai", "Software Products");
        Company c2 = new Company("C102", "Infosys Limited", "Pune", "IT Services");
        Company c3 = new Company("C103", "Tata Consultancy Services", "Mumbai", "IT Consulting");

        companyList.add(c1);
        companyList.add(c2);
        companyList.add(c3);

        // 3. Sample Jobs (ArrayList + TreeMap)
        Job j1 = new Job("J101", "C101", "Java Backend Developer", 9.5, 7.5, new String[]{"Java", "SQL"});
        Job j2 = new Job("J102", "C102", "Systems Engineer", 6.5, 7.0, new String[]{"Java", "Communication"});
        Job j3 = new Job("J103", "C103", "Graduate Trainee", 4.5, 6.5, new String[]{"C++", "HTML"});
        Job j4 = new Job("J104", "C101", "Full Stack Engineer", 12.0, 8.5, new String[]{"Java", "React", "SQL"});

        jobList.add(j1);
        jobList.add(j2);
        jobList.add(j3);
        jobList.add(j4);

        rebuildJobTreeMap();

        // 4. Sample Applications (LinkedList)
        Application a1 = new Application("APP-101", "S101", "J101", "Shortlisted");
        Application a2 = new Application("APP-102", "S103", "J104", "Selected");
        Application a3 = new Application("APP-103", "S102", "J102", "Applied");

        applicationList.add(a1);
        applicationList.add(a2);
        applicationList.add(a3);

        // 5. Sample Interviews (LinkedList)
        Interview inv1 = new Interview("INT-101", "APP-101", "2026-10-15", "10:30 AM", "Online");
        Interview inv2 = new Interview("INT-102", "APP-102", "2026-10-10", "02:00 PM", "Offline");

        interviewList.add(inv1);
        interviewList.add(inv2);
    }

    // =========================================================================
    // MAIN METHOD (Program Entry Point: Module I)
    // =========================================================================
    public static void main(String[] args) {
        // Set cross-platform look and feel for uniform modern look
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Launch GUI on the Event Dispatch Thread (Swing standard practice)
        SwingUtilities.invokeLater(() -> {
            StudentPlacementManagementSystem app = new StudentPlacementManagementSystem();
            app.setVisible(true);
        });
    }
}
