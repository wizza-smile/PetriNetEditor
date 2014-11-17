package view.figures;

public interface Selectable {

    public String getId();
    public void setSelected(boolean selected);
    public void updatePosition();
    public boolean isSelected();

}
