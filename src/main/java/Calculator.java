/**
 * Created by y.golota on 30.09.2016.
 */

import com.sun.java.swing.plaf.motif.MotifBorders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Calculator {

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
        String equ = "";
        String Result = "";

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
                System.out.println("brackets = " + brackets);
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

            System.out.println("ParseAndSolvTrigs in = " + equ);

            equ = equ.replace("*", "U*");
            equ = equ.replace("/", "U/");

            System.out.println("equ = " + equ);

            String[] args = equ.split("U");

            for (int i = 0; i < args.length; i++) {
                if (args[i].contains("sin")) {
                    //args[i] = args[i].replace("+-", "-");
                    //args[i] = args[i].replace("--", "-");
                    int t = args[i].indexOf('n') + 1;
                    String sin_arg = args[i].substring(t);
                    System.out.println("sin_arg = " + sin_arg);
                    args[i] = args[i].replace("sin" + sin_arg, Math.sin(Double.parseDouble(sin_arg)) + "");
                    System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("cos")) {
                    //args[i] = args[i].replace("+-", "-");
                    //args[i] = args[i].replace("--", "-");
                    int t = args[i].indexOf('s') + 1;
                    String cos_arg = args[i].substring(t);
                    System.out.println("cos_arg = " + cos_arg);
                    args[i] = args[i].replace("cos" + cos_arg, Math.cos(Double.parseDouble(cos_arg)) + "");
                    System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("tg")) {
                    int t = args[i].indexOf('g') + 1;
                    String tg_arg = args[i].substring(t);
                    System.out.println("tg_arg = " + tg_arg);
                    args[i] = args[i].replace("tg" + tg_arg, Math.tan(Double.parseDouble(tg_arg)) + "");
                    System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("log")) {
                    int t = args[i].indexOf('g') + 1;
                    String log_arg = args[i].substring(t);
                    System.out.println("log_arg = " + log_arg);
                    args[i] = args[i].replace("log" + log_arg, Math.log10(Double.parseDouble(log_arg)) + "");
                    System.out.println("args[" + i + "] = " + args[i]);
                }
                if (args[i].contains("sqrt")) {
                    int t = args[i].indexOf('t') + 1;
                    String sqrt_arg = args[i].substring(t);
                    System.out.println("sqrt_arg = " + sqrt_arg);
                    args[i] = args[i].replace("sqrt" + sqrt_arg, Math.sqrt(Double.parseDouble(sqrt_arg)) + "");
                    System.out.println("args[" + i + "] = " + args[i]);
                }
                result = result + args[i];
            }

            result = result.replace("+-", "-");
            result = result.replace("--", "+");
            System.out.println("ParseAndSolvTrigs return = " + result);
            return result + "";
        }

        private String Solv(String equ)
        {
            double result = 0.0;

            System.out.println("Solv in = " + equ);
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

            System.out.println("Solv return = " + result + "");
            return result + "";
        }

        private double MultiplyAndDivide(String equ)
        {
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

        Solver()
        {
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

        Solver(String s)
        {
            Result = Parse(s);
        }
    }

    private class Graph {

        int x0 = 50;
        int y0 = 50;
        int xm;
        int ym;
        int gridStepX = 10;
        int gridStepY = 10;

        public Graph()
        {
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            JFrame frame = new JFrame("Graph...");
            frame.setSize((int) (0.8*dim.getWidth()), (int) (0.8*dim.getHeight()));
            frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            frame.setResizable(true);

            JPanel function = new JPanel();
            JTextPane functionField = new JTextPane();
            functionField.setText("sin(x)*sin(20*x)");
            functionField.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            JLabel functionLabel = new JLabel("F(x)=");

            JPanel fConditions = new JPanel();
            JLabel fStartPointLabel = new JLabel("Start point Xs=");

            JTextPane fStartPoint = new JTextPane();
            fStartPoint.setText("-6.28");
            fStartPoint.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));
            JLabel fEndPointLabel = new JLabel("End point Xe=");

            JTextPane fEndPoint = new JTextPane();
            fEndPoint.setText("6.28");
            fEndPoint.setBorder(new MotifBorders.BevelBorder(true, Color.LIGHT_GRAY, Color.LIGHT_GRAY));

            fConditions.setLayout(new GridLayout(25, 1));
            fConditions.add(fStartPointLabel);
            fConditions.add(fStartPoint);
            fConditions.add(fEndPointLabel);
            fConditions.add(fEndPoint);

            JButton bbuild = new JButton(("Build graph"));
            bbuild.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String function = functionField.getText();
                    String xs = fStartPoint.getText();
                    String xf = fEndPoint.getText();
                    FunctionGraphic graphic = new FunctionGraphic(function, xs, xf);
                    graphic.setVisible(true);
                    frame.add(graphic, BorderLayout.CENTER);
                    frame.setVisible(true);
                }
            });

            function.setLayout(new FlowLayout());
            function.add(functionLabel);
            function.add(functionField);

            frame.add(bbuild, BorderLayout.SOUTH);
            frame.add(fConditions, BorderLayout.EAST);
            frame.add(function, BorderLayout.NORTH);

            frame.setVisible(true);
        }

        private class FunctionGraphic extends JComponent
        {
            Graphics g;
            String function;
            String xs;
            String xf;
            double[][] matrix;

            FunctionGraphic(String function, String xs, String xf)
            {
                super();
                this.function = function;
                this.xs = xs;
                this.xf = xf;
                matrix = Calculate(function, xs, xf);
                this.repaint();
            }

            double[][] Calculate(String function, String xs, String xf)
            {
                int points = 600;
                double[][] matrix = new double[2][points];
                double dx = (Double.parseDouble(xf) - Double.parseDouble(xs)) / points;
                for (int i = 0; i < matrix[1].length; i++) {
                    matrix[0][i] = Double.parseDouble(xs) + i * dx;
                    matrix[1][i] = Double.parseDouble(new Solver(function.replace("x", matrix[0][i] + "")).Result);
                }
                return matrix;
            }


            public void paint(Graphics g)
            {
                DrawGridAndAxis(g);
                DrawGraph(g);
            }

            private void DrawGridAndAxis(Graphics g)
            {
                int dX = (super.getWidth() - 2 * x0) / gridStepX;
                int dY = (super.getHeight() - 2 * y0) / gridStepY;

                xm = x0 + gridStepX * dX;
                ym = y0 + gridStepY * dY;

                g.setColor(Color.LIGHT_GRAY);

                for (int i = 0; i <= gridStepX; i++)
                    g.drawLine(x0 + i * dX, y0, x0 + i * dX, ym);
                for (int i = 0; i <= gridStepY; i++)
                    g.drawLine(x0, y0 + i * dY, xm, y0 + i * dY);
            }

            private void DrawGraph(Graphics g)
            {
                int[][] scrMatrix = new int[2][matrix[1].length];

                double yMin = matrix[1][0];
                double yMax = matrix[1][0];
                for (int i = 0; i < matrix[1].length; i++) {
                    if (matrix[1][i] > yMax) yMax = matrix[1][i];
                    if (matrix[1][i] < yMin) yMin = matrix[1][i];
                }

                double ratioX = (xm - x0) / (matrix[0][matrix[0].length-1] - matrix[0][0]);
                double ratioY = (ym - y0) / (yMax - yMin);

                for (int i = 0; i < matrix[1].length; i++) {
                    scrMatrix[0][i] = (int) ((matrix[0][i] - matrix[0][0]) * ratioX);
                    scrMatrix[1][i] = (int) ((matrix[1][i] - yMin) * ratioY);
                }
                System.out.println("\nyMin=" + yMin + " yMax=" + yMax);
                System.out.println("ym=" + ym + " xm=" + xm);
                System.out.println("rx=" + ratioX + " ry=" + ratioY + "\n");

                g.setColor(Color.DARK_GRAY);

                for (int i = 0; i < scrMatrix[1].length - 1; i++)
                    g.drawLine(x0 + scrMatrix[0][i], ym - scrMatrix[1][i], x0 + scrMatrix[0][i+1], ym - scrMatrix[1][i+1]);

                g.setColor(Color.BLUE);
                g.drawString(yMin + "", x0 - 40, ym);
                g.drawString(yMax + "", x0 - 40, y0);
                g.setColor(Color.RED);
                g.drawString(matrix[0][0] + "", x0, ym + 20);
                g.drawString(matrix[0][matrix[0].length - 1] + "", xm, ym + 20);

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
        JFrame frame = new JFrame("The Calculator");
        frame.setSize(480, 240);
        frame.setLocation(420, 300);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

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

        frame.add(key_panel, BorderLayout.CENTER);
        frame.add(operation_panel, BorderLayout.EAST);
        frame.add(equal_panel, BorderLayout.SOUTH);

        frame.add(display, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}