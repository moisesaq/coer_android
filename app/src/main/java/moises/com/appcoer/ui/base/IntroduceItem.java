package moises.com.appcoer.ui.base;

public class IntroduceItem {
    private int image, title, subtitle;

    public IntroduceItem(int image, int title, int subtitle){
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getImage() {
        return image;
    }

    public int getSubtitle() {
        return subtitle;
    }

    public int getTitle() {
        return title;
    }

}
