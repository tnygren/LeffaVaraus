package com.vaadin;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.server.*;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringViewDisplay;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;

import java.awt.*;
import java.util.Objects;

import static com.vaadin.ElokuvaKortti.ELOKUVAT;
import static com.vaadin.Login.LOGINVIEW;
import static com.vaadin.Register.REGISTERVIEW;
import static com.vaadin.YllapitoElokuvat.YLLAPITOELOKUVAVIEW;
import static com.vaadin.OmatVaraukset.OMATVARAUKSET;
import static com.vaadin.YllapitoNaytokset.YLLAPITONAYTOKSETVIEW;
import static com.vaadin.YksittainenElokuva.YKSITTAINENELOKUVAVIEW;

/**
 * This UI is the application entry point. A UI may either represent a browser window
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@SpringUI
@Theme("valo")
@SpringViewDisplay
public class MyUI extends UI implements ViewDisplay {
    private VerticalLayout layout;

    @Override
    protected void init(VaadinRequest request) {
        setSizeFull();
        final VerticalLayout root = new VerticalLayout();
        String username = String.valueOf(getSession().getAttribute("user"));
        HorizontalLayout otsikko = new HorizontalLayout(getOtsikko());
        root.addComponent(otsikko);
        root.setComponentAlignment(otsikko, Alignment.MIDDLE_CENTER);
        HorizontalLayout userBar = new HorizontalLayout(getCurrentUser(username));
        root.addComponent(userBar);
        root.setComponentAlignment(userBar, Alignment.MIDDLE_RIGHT);
        root.setSizeFull();
        setContent(root);

        final CssLayout navigationBar = new CssLayout();
        navigationBar.addStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
        navigationBar.addComponents(getMenubar());
        navigationBar.setWidth("100%");
        root.addComponent(navigationBar);

        layout = new VerticalLayout();
        layout.setSizeFull();
        root.addComponent(layout);
        root.setExpandRatio(layout, 1.0f);
        getUI().getNavigator().navigateTo(ELOKUVAT);

    }

    // FIXME Ei päivitä automaattisesti näkymään...
    private Component getCurrentUser(String username) {
        HorizontalLayout currentUserBar = new HorizontalLayout();
        Label currentUser;
        Button logout = new Button("Kirjaudu ulos",this::logout);
        if (!Objects.equals(username, "null")) {
            currentUser = new Label("Moikka "+username  );
            currentUserBar.addComponents(currentUser, logout);
        } else {
            currentUser = new Label("");
            currentUserBar.addComponents(currentUser);
        }
        return currentUserBar;
    }

    private void logout(Button.ClickEvent event) {
        getSession().setAttribute("user", null);
    }

    private static Label getOtsikko() {
        final Label Otsikko = new Label("Elokuvan varaaminen");
        Otsikko.addStyleName("title");
        return Otsikko;
    }

    private MenuBar getMenubar() {
        MenuBar barmenu = new MenuBar();
        barmenu.setStyleName("topmenu");
        barmenu.setSizeFull();

        barmenu.addItem(ELOKUVAT,
                (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(ELOKUVAT));
        barmenu.addItem(OMATVARAUKSET,
                (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(OMATVARAUKSET));
        MenuBar.MenuItem yllapitoValikko = barmenu.addItem("Ylläpito", null, null);
        yllapitoValikko.addItem(YLLAPITOELOKUVAVIEW,
                (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(YLLAPITOELOKUVAVIEW));
        yllapitoValikko.addItem(YLLAPITONAYTOKSETVIEW,
                (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(YLLAPITONAYTOKSETVIEW));
        barmenu.addItem(LOGINVIEW,
                (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(LOGINVIEW));
        barmenu.addItem(REGISTERVIEW,
                (MenuBar.Command) selectedItem -> getUI().getNavigator().navigateTo(REGISTERVIEW));
        return barmenu;
    }

    @Override
    public void showView(View view) {
        layout.removeAllComponents();
        layout.addComponent((Component) view);
    }
}
