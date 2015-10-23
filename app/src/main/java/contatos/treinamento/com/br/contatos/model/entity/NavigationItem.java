package contatos.treinamento.com.br.contatos.model.entity;

/**
 * Created by c1284521 on 23/10/2015.
 */
public class NavigationItem {

    private int icon;
    private String name;

    public NavigationItem(int icon, String name) {
        this.icon = icon;
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
