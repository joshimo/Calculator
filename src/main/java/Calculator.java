/**
 * Created by y.golota on 30.09.2016.
 */

import com.sun.java.swing.plaf.motif.MotifBorders;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {

    String display_text = "0";
    String A = "0";
    String B = "";
    String operation;

    boolean operation_performed = false;

    private String GetResult(String A, String B, String operation) {

        double result = 0;

        if (A.isEmpty()) A = "0";

        try {

            switch (operation) {
                case "+":
                    result = Double.parseDouble(A) + Double.parseDouble(B);
                    break;
                case "-":
                    result = Double.parseDouble(A) - Double.parseDouble(B);
                    break;
                case "*":
                    result = Double.parseDouble(A) * Double.parseDouble(B);
                    break;
                case "/":
                    result = Double.parseDouble(A) / Double.parseDouble(B);
                    break;
                case "sin":
                    result = Math.sin(Double.parseDouble(A));
                    break;
                case "cos":
                    result = Math.cos(Double.parseDouble(A));
                    break;
                case "tan":
                    result = Math.tan(Double.parseDouble(A));
                    break;
                case "ctan":
                    result = Math.cos(Double.parseDouble(A)) / Math.sin(Double.parseDouble(A));
                    break;
                case "x^2":
                    result = Double.parseDouble(A) * Double.parseDouble(A);
                    break;
                case "root":
                    result = Math.sqrt(Double.parseDouble(A));
                    break;
                case "log":
                    result = Math.log10(Double.parseDouble(A));
                    break;
                case "exp":
                    result = Math.exp(Double.parseDouble(A));
                    break;
                case "1/x":
                    result = 1.0 / Double.parseDouble(A);
                    break;
                case "ABS":
                    result = Math.abs(Double.parseDouble(A));
                    break;
                default:
                    break;
            }
        }
        catch (NumberFormatException e)
        {
            return "Divizion by zero error!";
        }
        return result + "";
    }

    private class Solver {
        private String equ = "";
        private String Result = "";

        private String Filter(String equ) {

            equ = equ.toLowerCase();
            equ = equ.replace(",", ".");
            equ = equ.replace(" ", "");
            equ = equ.replace("..", ".");
            equ = equ.replace(",,", ".");
            equ = equ.replace("=", "");
            equ = equ.replace((char) 92, '/');

            return equ;
        }

        private String Parse(String equ) {

            String result = "0";

            equ = Filter(equ);

            //Открываем скобки
            if(equ.contains("(") & equ.contains(")")) {

                int n = 0;
                int bStartIndex = 0;
                int bEndIndex;
                String brackets;
                //Ищем в выражении вложенные (внутренние) скобки
                while (true) {
                    if (equ.charAt(n) == '(') bStartIndex = n;
                    if (equ.charAt(n) == ')') {
                        bEndIndex = n + 1;
                        break;
                    }
                    n++ ;
                }

                brackets = equ.substring(bStartIndex, bEndIndex);
                //System.out.println("brackets = " + brackets);
                //Вычисляем выражение во внутренних скобках и заменяем выражение в скобках его вычислинным значением
                equ = equ.replace(brackets, Solv(equ.substring(bStartIndex + 1, bEndIndex - 1)));
                //Снова проверяем выражение на присутствие скобок
                equ = Parse(equ);
            }
            //Если все скобки открыты или в выражени их не было, вычисляем результат
            result = Solv(equ);
            return result;
        }

        private String ParseAndSolvTrigs(String equ) {

            String result = "";

            //System.out.println("ParseAndSolvTrigs in = " + equ);

            equ = equ.replace("*", "U*");
            equ = equ.replace("/", "U/");

            //System.out.println("equ = " + equ);

            String[] args = equ.split("U");

            for (int i = 0; i < args.length; i++) {
                if (args[i].contains("sin")) {
                    //args[i] = args[i].replace("+-", "-");
                    //args[i] = args[i].replace("--", "-");
                    int t = args[i].indexOf('n') + 1;
                    String sin_arg = args[i].substring(t);
                    //System.out.println("sin_arg = " + sin_arg);
                    args[i] = args[i].replace("sin" + sin_arg, Math.sin(Double.parseDouble(sin_arg)) + "");
                    //System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("cos")) {
                    //args[i] = args[i].replace("+-", "-");
                    //args[i] = args[i].replace("--", "-");
                    int t = args[i].indexOf('s') + 1;
                    String cos_arg = args[i].substring(t);
                    //System.out.println("cos_arg = " + cos_arg);
                    args[i] = args[i].replace("cos" + cos_arg, Math.cos(Double.parseDouble(cos_arg)) + "");
                    //System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("tg")) {
                    int t = args[i].indexOf('g') + 1;
                    String tg_arg = args[i].substring(t);
                    //System.out.println("tg_arg = " + tg_arg);
                    args[i] = args[i].replace("tg" + tg_arg, Math.tan(Double.parseDouble(tg_arg)) + "");
                    //System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("log")) {
                    int t = args[i].indexOf('g') + 1;
                    String log_arg = args[i].substring(t);
                    //System.out.println("log_arg = " + log_arg);
                    args[i] = args[i].replace("log" + log_arg, Math.log10(Double.parseDouble(log_arg)) + "");
                    //System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("sqrt")) {
                    int t = args[i].indexOf('t') + 1;
                    String sqrt_arg = args[i].substring(t);
                    //System.out.println("sqrt_arg = " + sqrt_arg);
                    args[i] = args[i].replace("sqrt" + sqrt_arg, Math.sqrt(Double.parseDouble(sqrt_arg)) + "");
                    //System.out.println("args[" + i + "] = " + args[i]);
                }
                result = result + args[i];
            }

            result = result.replace("+-", "-");
            result = result.replace("--", "+");
            //System.out.println("ParseAndSolvTrigs return = " + result);
            return result + "";
        }

        private String Solv(String equ) {
            double result = 0.0;

            //System.out.println("Solv in = " + equ);
            //Временно заменяем показатели степени
            equ = equ.replace("E+", "Z");
            equ = equ.replace("e+", "Z");
            equ = equ.replace("E-", "z");
            equ = equ.replace("e-", "z");
            equ = equ.replace("+-", "UM");
            equ = equ.replace("*-", "MM");
            equ = equ.replace("/-", "DM");
            equ = equ.replace("--", "UP");
            equ = equ.replace("+", "U+");

            equ = equ.replace("sin-", "sinM");
            equ = equ.replace("cos-", "cosM");
            equ = equ.replace("tg-", "tgM");
            equ = equ.replace("ctg-", "ctgM");
            equ = equ.replace("log-", "logM");
            equ = equ.replace("exp-", "expM");

            equ = equ.replace("-", "U-");

            equ = equ.replace("expM", "exp-");
            equ = equ.replace("logM", "log-");
            equ = equ.replace("ctgM", "ctg-");
            equ = equ.replace("tgM", "tg-");
            equ = equ.replace("cosM", "cos-");
            equ = equ.replace("sinM", "sin-");

            equ = equ.replace("UP", "U+");
            equ = equ.replace("UM", "U-");
            equ = equ.replace("MM", "*-");
            equ = equ.replace("DM", "/-");
            //Возвращаем показатели степени на место
            equ = equ.replace("Z", "E+");
            equ = equ.replace("z", "E-");

            String[] args = equ.split("U");

            //Проверяем каждый аргумент на тригонометрию
            for (int i = 0; i < args.length; i++)
                args[i] = ParseAndSolvTrigs(args[i]);

            //Если аргумент функции Solv просто отрицательное число, возвращаем его как результат
            if ((args[0].isEmpty())&(args.length == 2))
                if ((!args[1].contains("*"))&(!args[1].contains("/"))) return args[1];

            if ((args[0].isEmpty())&(args.length == 2))
                if ((args[1].contains("*"))|(args[1].contains("/"))) return MultiplyAndDivide(args[1]) + "";

            //Если первое число в аргументе Solv отрицательное, то args[0] будет пустым. Вместо него присваиваем 0;
            if ((args[0].isEmpty())&(args.length > 2)) args[0] = "0";

            //Складываем последовательно все аргументы. Если в аргументе присутствует операция умножения или деления
            //сначала вычисляем ее, затем прибваляем к общему результату
            for (int i = 0; i < args.length; i++)
                if ((!args[i].contains("*"))&(!args[i].contains("/")))
                    result = result + Double.parseDouble(args[i]);
                else
                    result = result + MultiplyAndDivide(args[i]);

            //System.out.println("Solv return = " + result + "");
            return result + "";
        }

        private double MultiplyAndDivide(String equ) {
            double result = 1.0;

            equ = equ.replace("*", "UM");
            equ = equ.replace("/", "UD");

            String[] args = equ.split("U");

            result = Double.parseDouble(args[0]);
            for (int i = 0; i < args.length; i++) {
                if (args[i].contains("M"))
                    result = result * Double.parseDouble(args[i].replace("M", ""));
                if (args[i].contains("D"))
                    result = result / Double.parseDouble(args[i].replace("D", ""));
            }
            return result;
        }

        Solver() {
            JFrame frame = new JFrame("Solver");
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.setSize(320, 120);
            frame.setLocation(500, 400);
            frame.setResizable(false);

            JTextPane display = new JTextPane();
            display.setEditable(true);
            display.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyChar() == '\n') {
                        equ=display.getText();
                        String result= Parse(equ);
                        JOptionPane.showMessageDialog(null, result);
                        equ = "";
                    }
                    if (e.getKeyChar() == 27) {
                        frame.setVisible(false);
                        equ = "";
                    }
                }
            });

            JButton eq = new JButton("=");
            eq.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    equ = display.getText();
                    String result = Parse(equ);
                    JOptionPane.showMessageDialog(null, result);
                    equ = "";
                }
            });

            frame.add(eq, BorderLayout.SOUTH);
            frame.add(display, BorderLayout.NORTH);
            frame.setVisible(true);
        }

        Solver(String s) {
            Result = Parse(s);
        }
    }

    private class Graph extends JFrame {

        private String function = "sin(x)*sin(50*x)";
        private Double Xs = -6.28;
        private Double Xe = 6.28;

        private int x0 = 60;
        private int y0 = 50;
        private int xm;
        private int ym;
        private Integer gridXnumber = 15;
        private Integer gridYnumber = 15;
        private Integer points = 100;

        private Double accuracy = 0.01;

        private boolean drawGridX = true;
        private boolean drawGridY = true;
        private boolean isRepaint = false;

        private int L;
        private int H;

        private JPanel fPanel;
        private JButton bbuild;
        private JPanel fConditions;
        private JCheckBox showGridsX;
        private JCheckBox showGridsY;

        private FunctionGraphic graphic;

        public Graph() {
            super("Graph...");
            Calculator.this.setVisible(false);

            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            H = (int) (0.7 * dim.getHeight());
            L = (int) (0.7 * dim.getWidth());

            this.setSize(L, H);
            this.setLayout(null);
            this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            this.setResizable(true);

            this.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    H = e.getComponent().getHeight();
                    L = e.getComponent().getWidth();
                    System.out.println("Width = " + L + ", Height = " + H);
                    isRepaint = true;
                    initGraph(L, H, isRepaint);
                }

                @Override
                public void componentHidden(ComponentEvent e) {
                    super.componentHidden(e);
                    Calculator.this.setVisible(true);
                }
            });

            initGraph(L, H, false);
        }

        private void initGraph (int L, int H, boolean repaint) {

            if (fPanel != null) this.remove(fPanel);
            if (bbuild != null) this.remove(bbuild);
            if (fConditions != null) this.remove(fConditions);

            fPanel = new JPanel();
            fPanel.setLayout(null);
            fPanel.setBounds(10, 10, (int) (0.35 * L + 60) - 20, 70);

            JTextPane functionField = new JTextPane();
            functionField.setBounds(40, 0, (int) (0.35 * L), 70);
            functionField.setFont(new Font("Dialog", Font.PLAIN, 16));
            functionField.setText(function);
            functionField.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));

            JLabel functionLabel = new JLabel("F(x) =");
            functionLabel.setBounds(0, 22, 35, 25);
            fPanel.add(functionLabel);
            fPanel.add(functionField);

            fConditions = new JPanel();
            fConditions.setLayout(null);
            //fConditions.setBorder(new MotifBorders.BevelBorder(false, Color.black, Color.LIGHT_GRAY));
            fConditions.setBounds((int)(0.35 * L + 80), 10, 470, 70);

            JLabel fStartPointLabel = new JLabel("X0 =");
            fStartPointLabel.setBounds(0, 0, 30, 25);
            fConditions.add(fStartPointLabel);

            JTextPane fStartPoint = new JTextPane();
            fStartPoint.setFont(new Font("Dialog", Font.PLAIN, 16));
            fStartPoint.setText(Xs.toString());
            fStartPoint.setBounds(35, 0, 90, 25);
            fStartPoint.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            fConditions.add(fStartPoint);

            JLabel fEndPointLabel = new JLabel("X1 =");
            fEndPointLabel.setBounds(0, 45, 30, 25);
            fConditions.add(fEndPointLabel);

            JTextPane fEndPoint = new JTextPane();
            fEndPoint.setFont(new Font("Dialog", Font.PLAIN, 16));
            fEndPoint.setText(Xe.toString());
            fEndPoint.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            fEndPoint.setBounds(35, 45, 90, 25);
            fConditions.add(fEndPoint);

            showGridsX = new JCheckBox("Grid X");
            showGridsX.setBounds(180, 0, 60, 25);
            showGridsX.setSelected(drawGridX);
            showGridsX.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    drawGridX = showGridsX.isSelected();
                    Graph.this.repaint(0, 0, L, H);
                }
            });
            fConditions.add(showGridsX);

            showGridsY = new JCheckBox("Grid Y");
            showGridsY.setBounds(180, 45, 60, 25);
            showGridsY.setSelected(drawGridY);
            showGridsY.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    drawGridY = showGridsY.isSelected();
                    Graph.this.repaint(0, 0, L, H);
                }
            });
            fConditions.add(showGridsY);

            JTextPane gridsXnumberField = new JTextPane();
            gridsXnumberField.setFont(new Font("Dialog", Font.PLAIN, 16));
            gridsXnumberField.setText(gridXnumber.toString());
            gridsXnumberField.setBounds(240, 0, 30, 25);
            gridsXnumberField.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            fConditions.add(gridsXnumberField);

            JTextPane gridsYnumberField = new JTextPane();
            gridsYnumberField.setFont(new Font("Dialog", Font.PLAIN, 16));
            gridsYnumberField.setText(gridYnumber.toString());
            gridsYnumberField.setBounds(240, 45, 30, 25);
            gridsYnumberField.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            fConditions.add(gridsYnumberField);

            JLabel pointsNumber = new JLabel("Points number");
            pointsNumber.setBounds(320, 0, 90, 25);
            fConditions.add( pointsNumber);

            JTextPane pointsNumberField = new JTextPane();
            pointsNumberField.setFont(new Font("Dialog", Font.PLAIN, 16));
            pointsNumberField.setText(points.toString());
            pointsNumberField.setBounds(410, 0, 50, 25);
            pointsNumberField.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            fConditions.add(pointsNumberField);

            JLabel xAccuracy = new JLabel("X accuracy");
            xAccuracy.setBounds(320, 45, 90, 25);
            fConditions.add(xAccuracy);

            JTextPane accuracyField = new JTextPane();
            accuracyField.setFont(new Font("Dialog", Font.PLAIN, 16));
            accuracyField.setText(accuracy.toString());
            accuracyField.setBounds(410, 45, 50, 25);
            accuracyField.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            fConditions.add(accuracyField);

            bbuild = new JButton(("Build graph"));
            bbuild.setBounds((int) (0.35 * L) + 600, 22, 100, 40);
            bbuild.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    function = functionField.getText();
                    function = function.replace((char) 10, ' ');
                    function = function.replace(" ", "");
                    function = function.replace(",", ".");
                    isRepaint = false;
                    try {
                        Xs = Double.parseDouble(Filter(fStartPoint.getText()));
                    }
                    catch (NumberFormatException nfe) {
                        while(true)
                            try {
                                Xs = Double.parseDouble(Filter(JOptionPane.showInputDialog(null, "Wrong X0 value!\nEnter another:")));
                                break;
                            }
                            catch (NumberFormatException ex) {}
                        fStartPoint.setText(Xs.toString());
                        initGraph(L, H, false);
                    }

                    try {
                        Xe = Double.parseDouble(Filter(fEndPoint.getText()));
                    }
                    catch (NumberFormatException nfe) {
                        Xe = Double.parseDouble(Filter(JOptionPane.showInputDialog(null, "Wrong X1 value!\nEnter another:")));
                        while(true)
                            try {
                                Xe = Double.parseDouble(Filter(JOptionPane.showInputDialog(null, "Wrong X1 value!\nEnter another:")));
                                break;
                            }
                            catch (NumberFormatException ex) {}
                        fEndPoint.setText(Xe.toString());
                        initGraph(L, H, false);
                    }

                    try {
                        gridXnumber = Integer.parseInt(Filter(gridsXnumberField.getText()));
                    }
                    catch (NumberFormatException nfe) {
                        while(true)
                            try {
                                gridXnumber = Integer.parseInt(Filter(JOptionPane.showInputDialog(null, "Wrong X axis grid number value!\nEnter another:")));
                                break;
                            }
                            catch (NumberFormatException ex) {}
                        gridsXnumberField.setText(gridXnumber.toString());
                        initGraph(L, H, false);
                    }

                    try {
                        gridYnumber = Integer.parseInt(Filter(gridsYnumberField.getText()));
                    }
                    catch (NumberFormatException nfe) {
                        while(true)
                            try {
                                gridYnumber = Integer.parseInt(Filter(JOptionPane.showInputDialog(null, "Wrong Y axis grid number value!\nEnter another:")));
                                break;
                            }
                            catch (NumberFormatException ex) {}
                        gridsYnumberField.setText(gridYnumber.toString());
                        initGraph(L, H, false);
                    }

                    try {
                        points = Integer.parseInt(Filter(pointsNumberField.getText()));
                    }
                    catch (NumberFormatException nfe) {
                        while(true)
                            try {
                                points = Integer.parseInt(Filter(JOptionPane.showInputDialog(null, "Wrong points number value!\nEnter another:")));
                                break;
                            }
                            catch (NumberFormatException ex) {}
                        pointsNumberField.setText(points.toString());
                        initGraph(L, H, false);
                    }

                    try {
                        accuracy = Double.parseDouble(Filter(accuracyField.getText()));
                    }
                    catch (NumberFormatException nfe) {
                        while(true)
                            try {
                                accuracy = Double.parseDouble(Filter(JOptionPane.showInputDialog(null, "Wrong accuracy value!\nEnter another:")));
                                break;
                            }
                            catch (NumberFormatException ex) {}
                        accuracyField.setText(accuracy.toString());
                        initGraph(L, H, false);
                    }

                    if (graphic != null) Graph.this.remove(graphic);

                    graphic = new FunctionGraphic(function, Xs, Xe, isRepaint);
                    graphic.setBounds(15, 50, L - 30, H - 80);
                    Graph.this.add(graphic);
                    Graph.this.repaint(0, 0, L, H);
                    Graph.this.revalidate();
                    Graph.this.setVisible(true);
                }
            });

            if (isRepaint)
            if (graphic != null) {
                graphic.setBounds(15, 50, L - 30, H - 80);
                Graph.this.repaint(0, 0, L, H);
                Graph.this.revalidate();
                Graph.this.setVisible(true);
            }

            this.add(bbuild);
            this.add(fConditions);
            this.add(fPanel);
            this.setVisible(true);
        }

        private String Filter(String equ) {
            equ = equ.toLowerCase();
            equ = equ.replace(",", ".");
            equ = equ.replace(",,", ".");
            equ = equ.replace(" ", "");
            equ = equ.replace("..", ".");
            equ = equ.replace("=", "");
            equ = equ.replace("+", "");
            equ = equ.replace("--", "-");
            return equ;
        }

        private class FunctionGraphic extends JComponent {
            private String function;
            private Double xs;
            private Double xf;
            private double yMin;
            private double yMax;
            private double xMin;
            private double xMax;

            private double[][] matrix;

            FunctionGraphic(String function, Double xs, Double xf, boolean isRepaint) {
                super();
                this.function = function;
                this.xs = xs;
                this.xf = xf;
                if (!isRepaint) matrix = Calculate();
                this.repaint();
            }

            double[][] Calculate() {

                if (points < (xf - xs) / accuracy)
                    points = (int) ((xf - xs) / accuracy);

                double[][] matrix = new double[2][points + 1];
                double dx = (xf - xs) / points;
                for (int i = 0; i < matrix[1].length; i ++) {
                    matrix[0][i] = xs + i * dx;
                    matrix[1][i] = Double.parseDouble(new Solver(function.replace("x", matrix[0][i] + "")).Result);
                }

                //for (int i = 0; i < matrix[0].length; i ++)
                //    System.out.println("X=" + matrix[0][i] + " Y=" + matrix[1][i]);
                return matrix;
            }

            public void paint(Graphics g) {
                DrawGridAndAxis(g);
                DrawGraph(g);
            }

            private void DrawGridAndAxis(Graphics g) {
                yMin = matrix[1][0];
                yMax = matrix[1][0];
                xMin = matrix[0][0];
                xMax = matrix[0][points];

                for (int i = 0; i < matrix[1].length; i ++) {
                    if (matrix[1][i] > yMax) yMax = matrix[1][i];
                    if (matrix[1][i] < yMin) yMin = matrix[1][i];
                }

                int dX = (super.getWidth() - 2 * x0) / gridXnumber;
                int dY = (super.getHeight() - 2 * y0) / gridYnumber;

                xm = x0 + gridXnumber * dX;
                ym = y0 + gridYnumber * dY;

                g.setColor(Color.LIGHT_GRAY);

                String[] xAxisMarks = new String[gridXnumber + 1];
                String[] yAxisMarks = new String[gridYnumber + 1];

                for (int i = 0; i <= gridXnumber; i ++)
                    xAxisMarks[i] = Round(xMin + i * (xMax - xMin) / gridXnumber);
                for (int i = 0; i <= gridYnumber; i ++)
                    yAxisMarks[i] = Round(yMin + i * (yMax - yMin) / gridYnumber);

                if (drawGridX || drawGridY) {
                    if (drawGridX)
                        for (int i = 0; i <= gridXnumber; i ++) {
                            g.drawLine(x0 + i * dX, y0, x0 + i * dX, ym);
                            g.drawString(xAxisMarks[i], x0 + i * dX  - (int) (3.5 * xAxisMarks[i].length()), ym + 20);
                        }
                    if (drawGridY)
                        for (int i = 0; i <= gridYnumber; i ++) {
                            g.drawLine(x0, y0 + i * dY, xm, y0 + i * dY);
                            g.drawString(yAxisMarks[i], x0 - 7 * yAxisMarks[i].length(), ym - i * dY + 5);
                        }
                }

                g.drawRect(x0, y0, xm - x0, ym - y0);

                if ((yMin <= 0) && (yMax >= 0)) {
                    g.setColor(Color.BLACK);
                    double coef = Math.abs(yMin) / (Math.abs(yMin) + yMax);
                    System.out.println("coefY="+coef);
                    g.drawLine(xm, y0 + (int) ((ym-y0) * (1 - coef)), xm - 15, y0 + (int) ((ym-y0) * (1 - coef)) - 5);
                    g.drawLine(xm, y0 + (int) ((ym-y0) * (1 - coef)), xm - 15, y0 + (int) ((ym-y0) * (1 - coef)) + 5);
                    g.drawLine(x0, y0 + (int) ((ym-y0) * (1 - coef)), xm, y0 + (int) ((ym-y0) * (1 - coef)));
                }
                if ((xMin <= 0) && (xMax >= 0)) {
                    g.setColor(Color.BLACK);
                    double coef = Math.abs(xMin) / (Math.abs(xMin) + xMax);
                    System.out.println("coefX="+coef);
                    g.drawLine(x0 + (int) ((xm - x0) * coef), y0, x0 + (int) ((xm - x0) * coef), ym);
                    g.drawLine(x0 + (int) ((xm - x0) * coef), y0, x0 + (int) ((xm - x0) * coef) - 5, y0 + 15);
                    g.drawLine(x0 + (int) ((xm - x0) * coef), y0, x0 + (int) ((xm - x0) * coef) + 5, y0 + 15);
                }

                g.setColor(Color.BLUE);
                g.drawString(Round(yMin), x0 - 7 * Round(yMin).length(), ym + 5);
                g.drawString(Round(yMax) + "", x0 - 7 * Round(yMax).length(), y0 + 5);
                g.setColor(Color.RED);
                g.drawString(Round(xMin), x0 - (int) (3.5 * Round(xMin).length()), ym + 20);
                g.drawString(Round(xMax), xm - (int) (3.5 * Round(xMax).length()), ym + 20);
            }

            private String Round(double d) {
                if (Math.abs(d) >= 1000) return (Math.round(10.0 * d)) / 10.0 + "";
                if ((Math.abs(d) <= 1000) && (Math.abs(d) > 10)) return (Math.round(100.0 * d)) / 100.0 + "";
                if ((Math.abs(d) <= 10) && (Math.abs(d) > 0.1)) return (Math.round(1000.0 * d)) / 1000.0 + "";
                if ((Math.abs(d) <= 0.1) && (Math.abs(d) > 0.001)) return (Math.round(10000.0 * d)) / 10000.0 + "";
                else return (Math.round(100000.0 * d)) / 100000.0 + "";
            }

            private void DrawGraph(Graphics g) {
                int[][] scrMatrix = new int[2][matrix[1].length];

                double ratioX = (xm - x0) / (matrix[0][matrix[0].length-1] - matrix[0][0]);
                double ratioY = (ym - y0) / (yMax - yMin);

                for (int i = 0; i < matrix[1].length; i++) {
                    scrMatrix[0][i] = (int) ((matrix[0][i] - matrix[0][0]) * ratioX);
                    scrMatrix[1][i] = (int) ((matrix[1][i] - yMin) * ratioY);
                }
                System.out.println("\nyMin=" + yMin + " yMax=" + yMax);
                System.out.println("ym=" + ym + " xm=" + xm);
                System.out.println("rx=" + ratioX + " ry=" + ratioY + "\n");

                g.setColor(Color.BLUE);

                for (int i = 0; i < scrMatrix[1].length - 1; i++)
                    g.drawLine(x0 + scrMatrix[0][i], ym - scrMatrix[1][i], x0 + scrMatrix[0][i+1], ym - scrMatrix[1][i + 1]);
            }
        }
    }

    private void Clear() {
        A = "0";
        B = "";
        operation_performed = false;
        display_text = "0";
    }

    private void AddDigit(String digit) {
        if (display_text.equals("0"))
            display_text = digit;
        else
        if (display_text.equals("Divizion by zero error!")){
            Clear();
            display_text = digit;
        }
        else
            display_text = display_text+digit;

        if (operation_performed)
            B = B + digit;
        else
            A = A + digit;
    }

    private void SetOperation(String operation) {
        if (!operation_performed) {
            this.operation = operation;
            operation_performed = true;
            display_text = display_text + operation;
        }
        else {
            A = GetResult(A, B, this.operation);
            B = "";
            this.operation = operation;
            display_text = A + operation;
            operation_performed = true;
        }
    }

    public Calculator() {
        super("The Calculator");
        this.setSize(480, 240);
        this.setLocation(420, 300);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel key_panel = new JPanel();
        key_panel.setLayout(new GridLayout(4, 4));

        JPanel operation_panel = new JPanel();
        operation_panel.setLayout(new GridLayout(4, 4));

        JPanel equal_panel = new JPanel();
        equal_panel.setLayout(new GridLayout(1, 3));

        JTextPane display = new JTextPane();
        display.setText(display_text);
        display.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                char c=e.getKeyChar();
                switch (c){
                    case '1':
                        AddDigit("1");
                        display.setText(display_text);
                        break;
                    case '2':
                        AddDigit("2");
                        display.setText(display_text);
                        break;
                    case '3':
                        AddDigit("3");
                        display.setText(display_text);
                        break;
                    case '4':
                        AddDigit("4");
                        display.setText(display_text);
                        break;
                    case '5':
                        AddDigit("5");
                        display.setText(display_text);
                        break;
                    case '6':
                        AddDigit("6");
                        display.setText(display_text);
                        break;
                    case '7':
                        AddDigit("7");
                        display.setText(display_text);
                        break;
                    case '8':
                        AddDigit("8");
                        display.setText(display_text);
                        break;
                    case '9':
                        AddDigit("9");
                        display.setText(display_text);
                        break;
                    case '0':
                        AddDigit("0");
                        display.setText(display_text);
                        break;
                    case '+':
                        SetOperation("+");
                        display.setText(display_text);
                        break;
                    case '-':
                        SetOperation("-");
                        display.setText(display_text);
                        break;
                    case '*':
                        SetOperation("*");
                        display.setText(display_text);
                        break;
                    case '/':
                        SetOperation("/");
                        display.setText(display_text);
                        break;
                    case '\n':
                        if ((!A.isEmpty())&(operation_performed)) {
                            A = GetResult(A, B, operation);
                            display.setText(A);
                            B = "";
                            operation_performed = false;
                        }
                        else {
                            display_text = "0";
                            A = "0";
                            display.setText(display_text);
                        }
                        break;
                    case 27:
                        Clear();
                        display.setText(display_text);
                        break;

                    default:
                        break;
                }
            }
        });

        display.setEditable(false);
        display.setFont(new Font("Dialog", Font.BOLD, 24));

        JButton b0=new JButton("0");
        b0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if ((!display_text.equals("0"))|(!B.equals("0")))
                    AddDigit("0");
                else
                    display_text = "0";

                display.setText(display_text);
            }
        });
        JButton b1=new JButton("1");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("1");
                display.setText(display_text);
            }
        });
        JButton b2=new JButton("2");
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("2");
                display.setText(display_text);
            }
        });
        JButton b3=new JButton("3");
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("3");
                display.setText(display_text);
            }
        });
        JButton b4=new JButton("4");
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("4");
                display.setText(display_text);
            }
        });
        JButton b5=new JButton("5");
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("5");
                display.setText(display_text);
            }
        });
        JButton b6=new JButton("6");
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("6");
                display.setText(display_text);
            }
        });
        JButton b7=new JButton("7");
        b7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("7");
                display.setText(display_text);
            }
        });
        JButton b8=new JButton("8");
        b8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("8");
                display.setText(display_text);
            }
        });
        JButton b9=new JButton("9");
        b9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddDigit("9");
                display.setText(display_text);
            }
        });
        JButton bcomma=new JButton(".");
        bcomma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (display_text.equals("Divizion by zero error!"))
                {
                    Clear();
                    display.setText(display_text);
                }
                if (!A.contains(".")){
                    display_text = display_text + ".";
                    A = A + ".";
                }
                if ((!B.contains("."))&(operation_performed)){
                    display_text = display_text + ".";
                    B = B + ".";
                }
                display.setText(display_text);
            }
        });
        JButton bplus=new JButton("+");
        bplus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("+");
                display.setText(display_text);
            }
        });
        JButton bminus=new JButton("-");
        bminus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("-");
                display.setText(display_text);
            }
        });
        JButton bmulty=new JButton("*");
        bmulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("*");
                display.setText(display_text);
            }
        });
        JButton bdivide=new JButton("/");
        bdivide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("/");
                display.setText(display_text);
            }
        });
        JButton beq=new JButton("=");
        beq.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((!A.isEmpty())&(operation_performed)) {
                    A = GetResult(A, B, operation);
                    display.setText(A);
                    B = "";
                    operation_performed = false;
                }
                else {
                    display_text = "0";
                    A = "0";
                    display.setText(display_text);
                }
            }
        });

        JButton bcl=new JButton("C");
        bcl.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clear();
                display.setText(display_text);
            }
        });

        JButton bsin=new JButton("sin");
        bsin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("sin");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton bcos=new JButton("cos");
        bcos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("cos");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton btan=new JButton("tg");
        btan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("tan");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton bctan=new JButton("ctg");
        bctan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("ctan");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton bsqr=new JButton("x^2");
        bsqr.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("x^2");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton bsqrt=new JButton("root");
        bsqrt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("root");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton blog=new JButton("log");
        blog.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("log");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton bexp=new JButton("exp(x)");
        bexp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("exp");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });

        JButton brec=new JButton("1/x");
        brec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("1/x");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton babs=new JButton("abs");
        babs.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SetOperation("abs");
                A = GetResult(A, B, operation);
                display.setText(A);
                B = "";
                operation_performed = false;
            }
        });
        JButton bsolver=new JButton("SOLV");
        bsolver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Solver();
            }
        });
        JButton bgraph=new JButton("GRAPH");
        bgraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Graph();
            }
        });

        key_panel.add(b1);
        key_panel.add(b2);
        key_panel.add(b3);
        key_panel.add(b4);
        key_panel.add(b5);
        key_panel.add(b6);
        key_panel.add(b7);
        key_panel.add(b8);
        key_panel.add(b9);
        key_panel.add(b0);
        key_panel.add(bcomma);
        key_panel.add(bcl);

        operation_panel.add(bplus);
        operation_panel.add(bminus);
        operation_panel.add(bmulty);
        operation_panel.add(bdivide);
        operation_panel.add(bsin);
        operation_panel.add(bcos);
        operation_panel.add(btan);
        operation_panel.add(bctan);

        operation_panel.add(bsqr);
        operation_panel.add(bsqrt);
        operation_panel.add(blog);
        operation_panel.add(bexp);

        operation_panel.add(brec);
        operation_panel.add(babs);
        operation_panel.add(bsolver);
        operation_panel.add(bgraph);

        equal_panel.add(beq);

        this.add(key_panel, BorderLayout.CENTER);
        this.add(operation_panel, BorderLayout.EAST);
        this.add(equal_panel, BorderLayout.SOUTH);

        this.add(display, BorderLayout.NORTH);
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}