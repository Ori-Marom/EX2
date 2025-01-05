

public class SCell implements Cell {
    private String line;
    private int type;
    private int order;

    public SCell(String s) {
        setData(s);
    }

    @Override
    public String getData() {
        return this.line;
    }

    @Override
    public void setData(String s) {
        if (s == null || s.trim().isEmpty()) {
            this.line = Ex2Utils.EMPTY_CELL;
            this.type = Ex2Utils.TEXT;
            return;
        }

        this.line = s.trim();
        this.type = checkType(this.line);

        switch (this.type){
            case Ex2Utils.FORM:
                try {
                    double result = computeForm(this.line);
                    this.line = String.format("%.1f", result);
                } catch (IllegalArgumentException e) {
                    this.type = Ex2Utils.ERR_FORM_FORMAT;
                    this.line = Ex2Utils.ERR_FORM;
                } break;
            case Ex2Utils.NUMBER:
                try {
                    double number = Double.parseDouble(this.line);
                    this.line = String.format("%.1f", number);
                } catch (NumberFormatException e) {
                    this.type = Ex2Utils.ERR;
                    this.line = Ex2Utils.ERR_FORM;
                } break;
            case Ex2Utils.TEXT:
                this.line = s;
                this.type = Ex2Utils.TEXT; break;
            default:
                this.type = Ex2Utils.ERR;
                this.line = Ex2Utils.ERR_FORM;
        }

    }

    @Override
    public int getType() {
        return this.type;
    }

    @Override
    public void setType(int t) {
        this.type = t;
    }

    @Override
    public int getOrder() {
        if (type == Ex2Utils.NUMBER || type == Ex2Utils.TEXT) {
            return 0;
        } else if (type == Ex2Utils.FORM) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public void setOrder(int t) {
        this.order = t;
    }

    @Override
    public String toString() {
        return getData();
    }

    public double computeForm(String form) {
        if (!isForm(form)) throw new IllegalArgumentException("INVALID FORM");
        String finalForm = form.substring(1).trim();
        if (isNumber(finalForm)) return Double.parseDouble(finalForm);
        if (isValCell(finalForm)) throw new IllegalArgumentException("CELL REFERENCE ERR");
        if (finalForm.startsWith("(") && finalForm.endsWith(")")) {
            if (isMatchPar(finalForm, 0, finalForm.length() - 1)) {
                return computeForm("=" + finalForm.substring(1, finalForm.length() - 1));
            }
        }
        int index = indOfMainOp(finalForm);
        if (index != -1) {
            String leftSide = finalForm.substring(0, index).trim();
            String rightSide = finalForm.substring(index + 1).trim();
            char mainOp = finalForm.charAt(index);
            return calculator(computeForm("=" + leftSide), computeForm("=" + rightSide), mainOp);
        }
        throw new IllegalArgumentException("INVALID FORM");
    }

    private double calculator(double leftSide, double rightSide, char mainOp) {
        switch (mainOp) {
            case '+':
                return leftSide + rightSide;
            case '-':
                return leftSide - rightSide;
            case '*':
                return leftSide * rightSide;
            case '/':
                if (rightSide == 0) throw new ArithmeticException("INVALID DIVIDING");
                return leftSide / rightSide;
            default:
                throw new IllegalArgumentException("INVALID OPERATOR");
        }
    }

    private boolean isNumber(String text) {
        try {
            Double.parseDouble(text.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isText(String text) {
        return text != null && !text.isEmpty() && !text.startsWith("=") && !isNumber(text);
    }

    private boolean isForm(String text) {
        if (text == null || text.isEmpty() || !text.startsWith("="))
            return false;

        String form = text.substring(1).trim();
        if (isNumber(form)) return true;
        if (isValCell(form)) return true;
        if (!hasBalancedParentheses(form)) return false;

        try {
            return parseForm(form);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isValCell(String text) {
        if (text == null || text.length() < 2) return false;
        char column = Character.toUpperCase(text.charAt(0));
        if (column < 'A' || column > 'Z') return false;
        try {
            int row = Integer.parseInt(text.substring(1));
            return row >= 0 && row < 100;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean hasBalancedParentheses(String text) {
        int balance = 0;
        for (char c : text.toCharArray()) {
            if (c == '(') balance++;
            else if (c == ')') balance--;
            if (balance < 0) return false;
        }
        return balance == 0;
    }

    private boolean parseForm(String form) {
        form = form.trim();
        if (isNumber(form) || isValCell(form)) return true;
        if (form.startsWith("(") && form.endsWith(")")) {
            if (isMatchPar(form, 0, form.length() - 1)) {
                return parseForm(form.substring(1, form.length() - 1));
            }
        }
        int operatorIndex = indOfMainOp(form);
        if (operatorIndex != -1) {
            String leftSide = form.substring(0, operatorIndex).trim();
            String rightSide = form.substring(operatorIndex + 1).trim();
            return parseForm(leftSide) && parseForm(rightSide);
        }
        return false;
    }

    private boolean isMatchPar(String text, int start, int end) {
        int depth = 0;
        for (int i = start; i <= end; i++) {
            if (text.charAt(i) == '(') depth++;
            else if (text.charAt(i) == ')') depth--;
            if (depth == 0 && i != end) return false;
        }
        return depth == 0;
    }

    private int indOfMainOp(String text) {
        int index = -1;
        int level = 0;
        int lowP = Integer.MAX_VALUE;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') level++;
            else if (c == ')') level--;
            else if (level == 0) {
                int priority = getOpPr(c);
                if (priority <= lowP) {
                    lowP = priority;
                    index = i;
                }
            }
        }
        return index;
    }

    private int getOpPr(char c) {
        switch (c) {
            case '+':
                return 1;
            case '-':
                return 1;
            case '*':
                return 2;
            case '/':
                return 2;
            default:
                return Integer.MAX_VALUE;
        }
    }
    public int checkType(String s){
        if(s == null  ||s.trim().isEmpty()||  isText(s)) return Ex2Utils.TEXT;
        if(isNumber(s)) return Ex2Utils.NUMBER;
        if(isForm(s)) return Ex2Utils.FORM;
        return Ex2Utils.ERR_FORM_FORMAT;
    }
}