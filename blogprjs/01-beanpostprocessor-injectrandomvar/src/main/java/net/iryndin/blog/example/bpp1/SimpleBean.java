package net.iryndin.blog.example.bpp1;

public class SimpleBean {

    @InjectRandomInt(min = 1, max = 100)
    private int intField;

    public int getIntField() {
        return intField;
    }

    public void setIntField(int intField) {
        this.intField = intField;
    }
}
