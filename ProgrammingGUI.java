import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class ProgrammingGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private String[][] javaQuestions = {
        {"What is JVM short for?", "Java Virtual Machine", "Java Variable Method", "Joint Virtual Module", "Java Version Manager", "A", "JVM stands for Java Virtual Machine. It's the engine that runs Java bytecode and enables platform independence."},
        {"Which keyword is used to inherit a class in Java?", "implements", "inherits", "extends", "override", "C", "The 'extends' keyword is used in Java to inherit from a superclass. 'implements' is used for interfaces."},
        {"What is the default value of an int variable?", "null", "0", "undefined", "garbage", "B", "In Java, the default value of an int variable is 0. This applies when it's a class-level variable."}
    };

    private String[][] pythonQuestions = {
        {"What keyword is used to define a function in Python?", "func", "def", "function", "lambda", "B", "'def' is the keyword used to define a function in Python. It's short for 'define'."},
        {"What data type is the result of: 3 / 2?", "int", "double", "float", "long", "C", "In Python 3, division using '/' always returns a float, even if both operands are integers."},
        {"Which symbol is used for comments in Python?", "//", "<!-- -->", "/* */", "#", "D", "The '#' symbol is used to write single-line comments in Python."}
    };

    private String[][] cppQuestions = {
        {"What is the file extension for a C++ source file?", ".c", ".cpp", ".cc", ".cp", "B", "C++ source files typically use the '.cpp' extension. '.c' is for C language files."},
        {"Which operator is used to access members of a class?", "->", ":", ".", "::", "C", "The '.' operator is used to access members of a class or struct when using an object directly."},
        {"What is the keyword to define a constant variable?", "static", "final", "const", "define", "C", "'const' is the keyword used to declare constant variables in C++ that cannot be modified."}
    };

    private int currentQuestion = 0;
    private int score = 0;
    private int attempted = 0;
    private String[][] currentQuiz;
    private List<String[]> wrongAnswers = new ArrayList<>();

    private JLabel questionLabel;
    private JRadioButton[] options;
    private ButtonGroup optionGroup;
    private JButton nextButton;

    private JLabel resultLabel;
    private JTextArea explanationArea;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ProgrammingGUI().createAndShowGUI());
    }

    private void createAndShowGUI() {
        frame = new JFrame("Programming MCQ Quiz");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createWelcomePanel(), "Welcome");
        mainPanel.add(createQuizPanel(), "Quiz");
        mainPanel.add(createResultPanel(), "Result");
        mainPanel.add(createExplanationPanel(), "Explanation");

        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel welcomePanel = new JPanel(new GridLayout(5, 1, 10, 10));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel welcomeLabel = new JLabel("Welcome to the Programming MCQ Quiz!", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomePanel.add(welcomeLabel);

        welcomePanel.add(new JLabel("Choose a programming language:", SwingConstants.CENTER));

        JButton javaButton = new JButton("Java");
        JButton pythonButton = new JButton("Python");
        JButton cppButton = new JButton("C++");

        javaButton.addActionListener(e -> startQuiz(javaQuestions));
        pythonButton.addActionListener(e -> startQuiz(pythonQuestions));
        cppButton.addActionListener(e -> startQuiz(cppQuestions));

        welcomePanel.add(javaButton);
        welcomePanel.add(pythonButton);
        welcomePanel.add(cppButton);

        return welcomePanel;
    }

    private JPanel createQuizPanel() {
        JPanel quizPanel = new JPanel(new BorderLayout(10, 10));
        quizPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        questionLabel = new JLabel("Question", SwingConstants.CENTER);
        questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        quizPanel.add(questionLabel, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        options = new JRadioButton[4];
        optionGroup = new ButtonGroup();
        for (int i = 0; i < 4; i++) {
            options[i] = new JRadioButton();
            options[i].setFont(new Font("Arial", Font.PLAIN, 14));
            optionGroup.add(options[i]);
            optionsPanel.add(options[i]);
        }
        quizPanel.add(optionsPanel, BorderLayout.CENTER);

        nextButton = new JButton("Next");
        nextButton.addActionListener(e -> checkAnswer());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        quizPanel.add(buttonPanel, BorderLayout.SOUTH);

        return quizPanel;
    }

    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        resultLabel = new JLabel("", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultPanel.add(resultLabel, BorderLayout.CENTER);

        JButton explanationButton = new JButton("Show Explanations");
        explanationButton.addActionListener(e -> {
            showExplanations();
            cardLayout.show(mainPanel, "Explanation");
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(explanationButton);
        resultPanel.add(buttonPanel, BorderLayout.SOUTH);

        return resultPanel;
    }

    private JPanel createExplanationPanel() {
        JPanel explanationPanel = new JPanel(new BorderLayout(10, 10));
        explanationPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        explanationArea = new JTextArea();
        explanationArea.setEditable(false);
        explanationArea.setFont(new Font("Serif", Font.PLAIN, 14));
        explanationArea.setLineWrap(true);
        explanationArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(explanationArea);
        explanationPanel.add(scrollPane, BorderLayout.CENTER);

        return explanationPanel;
    }

    private void startQuiz(String[][] quizData) {
        this.currentQuiz = quizData;
        this.currentQuestion = 0;
        this.score = 0;
        this.attempted = 0;
        this.wrongAnswers.clear();
        loadQuestion();
        cardLayout.show(mainPanel, "Quiz");
    }

    private void loadQuestion() {
        if (currentQuestion < currentQuiz.length) {
            String[] q = currentQuiz[currentQuestion];
            questionLabel.setText("<html><div style='text-align:center;'>Q" + (currentQuestion + 1) + ": " + q[0] + "</div></html>");
            for (int i = 0; i < 4; i++) {
                options[i].setText(q[i + 1]);
                options[i].setSelected(false);
            }
            optionGroup.clearSelection();
        } else {
            showFinalScore();
        }
    }

    private void checkAnswer() {
        int selectedIndex = -1;
        for (int i = 0; i < 4; i++) {
            if (options[i].isSelected()) {
                selectedIndex = i;
                break;
            }
        }

        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select an option before proceeding.");
            return;
        }

        attempted++;
        String correct = currentQuiz[currentQuestion][5];
        char selected = (char) ('A' + selectedIndex);
        if (String.valueOf(selected).equals(correct)) {
            score++;
        } else {
            wrongAnswers.add(currentQuiz[currentQuestion]);
        }

        currentQuestion++;
        loadQuestion();
    }

    private void showFinalScore() {
        resultLabel.setText("<html><center>Quiz Completed!<br>" +
            "Total Questions Attempted: " + attempted + "<br>" +
            "Correct Answers: " + score + "<br>" +
            "Score: " + (score * 100 / attempted) + "%</center></html>");
        cardLayout.show(mainPanel, "Result");
    }

    private void showExplanations() {
        StringBuilder sb = new StringBuilder();
	 if (wrongAnswers.isEmpty()) {
            sb.append(" Great job! You answered all questions correctly.\n");
        } else {
            sb.append("Here are the explanations for the questions you missed:\n\n");
            for (String[] q : wrongAnswers) {
                sb.append("Q: ").append(q[0]).append("\n");
                sb.append("Correct Answer: ").append(q[q[5].charAt(0) - 'A' + 1]).append("\n");
                sb.append("Explanation: ").append(q[6]).append("\n\n");
            }
        }
        explanationArea.setText(sb.toString());
    }
}