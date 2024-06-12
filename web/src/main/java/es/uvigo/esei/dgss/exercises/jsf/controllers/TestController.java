package es.uvigo.esei.dgss.exercises.jsf.controllers;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Named(value="testController")
@SessionScoped
public class TestController implements Serializable {

    private Date date;
    private int operand1;
    private int operand2;
    private int result;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getOperand1() {
        return operand1;
    }

    public void setOperand1(int operand1) {
        this.operand1 = operand1;
    }

    public int getOperand2() {
        return operand2;
    }

    public void setOperand2(int operand2) {
        this.operand2 = operand2;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public TestController() {
    }

    @PostConstruct
    public void initDate() {
        date = Calendar.getInstance().getTime();
    }

    public String doAddition() {
        result = operand1 + operand2;
        return "index";
    }

}
