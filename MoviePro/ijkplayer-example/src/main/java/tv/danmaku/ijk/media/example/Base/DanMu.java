package tv.danmaku.ijk.media.example.Base;

public class DanMu {
    private String content;
    private float time;
    private int type;
    private int color;
    private int fontsize;

    public DanMu(String content, float time, int type, int color, int fontsize) {
        this.content = content;
        this.time = time;
        this.type = type;
        this.color = color;
        this.fontsize = fontsize;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getFontsize() {
        return fontsize;
    }

    public void setFontsize(int fontsize) {
        this.fontsize = fontsize;
    }
}
