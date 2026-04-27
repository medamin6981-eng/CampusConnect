package com.enspd.campusconnect;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;

import com.googlecode.lanterna.graphics.ThemeDefinition;
import com.googlecode.lanterna.graphics.ThemeStyle;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.table.Table;
import com.googlecode.lanterna.gui2.table.TableCellRenderer;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/*
 * ============================================================================
 * Dépendance Maven (pom.xml) à ajouter :
 * <dependency>
 *     <groupId>com.googlecode.lanterna</groupId>
 *     <artifactId>lanterna</artifactId>
 *     <version>3.1.1</version>
 * </dependency>
 *
 * Dépendance Gradle (build.gradle) à ajouter :
 * implementation 'com.googlecode.lanterna:lanterna:3.1.1'
 * ============================================================================
 */
public class CampusConnect {

    private static String[] menus = {
            "Dashboard", "Étudiants", "Enseignants", "Cours", "Groupes",
            "Note & Résultats", "Inscriptions", "Salles", "Emploi du temps"
    };
    private static int selectedIndex = 0;

    private static Label pageTitleLabel;
    private static Label timeLabel;

    private static List<Label> menuLabels = new ArrayList<>();

    private static Panel contentArea;
    private static MultiWindowTextGUI gui;

    private static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static ProgressBar roomUsageBar;

    public static void main(String[] args) {
        try {
            Terminal terminal = new DefaultTerminalFactory().createTerminal();
            Screen screen = new TerminalScreen(terminal);
            screen.startScreen();

            gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), new EmptySpace(TextColor.ANSI.BLACK));

            // --- Splash Screen: Initialisation ---
            BasicWindow splashWindow = new BasicWindow();
            splashWindow.setHints(Arrays.asList(Window.Hint.CENTERED));
            Panel splashPanel = new Panel(new LinearLayout(Direction.VERTICAL));
            splashPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
            splashPanel.addComponent(
                    new Label("Initialisation du système CampusConnect...").setForegroundColor(TextColor.ANSI.GREEN));
            splashPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
            ProgressBar splashProgress = new ProgressBar(0, 100, 40);
            splashPanel.addComponent(splashProgress);
            splashPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
            splashWindow.setComponent(splashPanel);

            gui.addWindow(splashWindow);

            for (int i = 0; i <= 100; i += 5) {
                splashProgress.setValue(i);
                gui.updateScreen();
                Thread.sleep(50);
            }
            gui.removeWindow(splashWindow);

            // --- Main Application ---
            BasicWindow mainWindow = new BasicWindow();
            mainWindow.setHints(Arrays.asList(Window.Hint.FULL_SCREEN, Window.Hint.NO_DECORATIONS));

            Panel rootPanel = new Panel(new BorderLayout());
            rootPanel.addComponent(buildHeader(), BorderLayout.Location.TOP);
            rootPanel.addComponent(buildSidebar(), BorderLayout.Location.LEFT);

            contentArea = new Panel(new BorderLayout());
            rootPanel.addComponent(contentArea, BorderLayout.Location.CENTER);

            rootPanel.addComponent(buildFooter(), BorderLayout.Location.BOTTOM);

            mainWindow.setComponent(rootPanel);

            // Global Key Interception
            mainWindow.addWindowListener(new WindowListenerAdapter() {
                @Override
                public void onInput(Window window, KeyStroke keyStroke, AtomicBoolean deliverEvent) {
                    KeyType type = keyStroke.getKeyType();
                    if (type == KeyType.Character
                            && (keyStroke.getCharacter() == 'q' || keyStroke.getCharacter() == 'Q')) {
                        System.exit(0);
                    } else if (type == KeyType.Escape) {
                        System.exit(0);
                    } else if (type == KeyType.ArrowUp) {
                        selectedIndex = Math.max(0, selectedIndex - 1);
                        updateSidebarSelection();
                        deliverEvent.set(false);
                    } else if (type == KeyType.ArrowDown) {
                        selectedIndex = Math.min(menus.length - 1, selectedIndex + 1);
                        updateSidebarSelection();
                        deliverEvent.set(false);
                    } else if (type == KeyType.Enter) {
                        navigateTo(menus[selectedIndex]);
                        deliverEvent.set(false);
                    }
                }
            });

            // Start clock thread
            scheduler.scheduleAtFixedRate(() -> {
                timeLabel.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()));
                try {
                    gui.updateScreen();
                } catch (Exception e) {
                }
            }, 0, 1, TimeUnit.SECONDS);

            navigateTo("Dashboard");

            gui.addWindowAndWait(mainWindow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Component buildHeader() {
        Panel header = new Panel(new BorderLayout());
        pageTitleLabel = new Label(" Dashboard ");
        pageTitleLabel.setForegroundColor(TextColor.ANSI.GREEN);
        timeLabel = new Label("00/00/0000 00:00:00");

        header.addComponent(pageTitleLabel, BorderLayout.Location.LEFT);
        header.addComponent(timeLabel, BorderLayout.Location.RIGHT);
        return header;
    }

    private static Component buildSidebar() {
        Panel sidebarPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        sidebarPanel.setPreferredSize(new TerminalSize(28, 20));

        String[] asciiTitle = {
                "  ____",
                " / ___|__ _ _ __ ___  ",
                "| |   / _` | '_ ` _ \\ ",
                "| |__| (_| | | | | | |",
                " \\____\\__,_|_| |_| |_|",
                "      ENSPD"
        };
        for (String line : asciiTitle) {
            sidebarPanel.addComponent(new Label(line).setForegroundColor(TextColor.ANSI.GREEN));
        }

        sidebarPanel.addComponent(new EmptySpace(new TerminalSize(1, 2)));

        Panel sidebarMenuItemsPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        for (String menu : menus) {
            Label l = new Label("  " + menu);
            menuLabels.add(l);
            sidebarMenuItemsPanel.addComponent(l);
        }
        updateSidebarSelection();

        sidebarPanel.addComponent(sidebarMenuItemsPanel);
        return sidebarPanel;
    }

    private static void updateSidebarSelection() {
        for (int i = 0; i < menuLabels.size(); i++) {
            Label l = menuLabels.get(i);
            if (i == selectedIndex) {
                l.setBackgroundColor(TextColor.Indexed.fromRGB(50, 50, 50)); // Fond sombre
                l.setForegroundColor(TextColor.ANSI.GREEN);
            } else {
                l.setBackgroundColor(TextColor.ANSI.DEFAULT);
                l.setForegroundColor(TextColor.ANSI.WHITE);
            }
        }
        try {
            if (gui != null)
                gui.updateScreen();
        } catch (Exception e) {
        }
    }

    private static Component buildFooter() {
        Panel footer = new Panel(new LinearLayout(Direction.HORIZONTAL));

        footer.addComponent(
                new Label(" Q ").setBackgroundColor(TextColor.ANSI.WHITE).setForegroundColor(TextColor.ANSI.BLACK));
        footer.addComponent(new Label(" Quitter | ").setForegroundColor(TextColor.ANSI.GREEN));

        footer.addComponent(new Label(" \u2191\u2193 ").setBackgroundColor(TextColor.ANSI.WHITE)
                .setForegroundColor(TextColor.ANSI.BLACK)); // ↑↓
        footer.addComponent(new Label(" Navigation | ").setForegroundColor(TextColor.ANSI.GREEN));

        footer.addComponent(
                new Label(" Enter ").setBackgroundColor(TextColor.ANSI.WHITE).setForegroundColor(TextColor.ANSI.BLACK));
        footer.addComponent(new Label(" Sélectionner | ").setForegroundColor(TextColor.ANSI.GREEN));

        footer.addComponent(
                new Label(" Tab ").setBackgroundColor(TextColor.ANSI.WHITE).setForegroundColor(TextColor.ANSI.BLACK));
        footer.addComponent(new Label(" Changer section ").setForegroundColor(TextColor.ANSI.GREEN));

        return footer;
    }

    private static void navigateTo(String page) {
        pageTitleLabel.setText(" " + page + " ");
        contentArea.removeAllComponents();

        if (page.equals("Dashboard")) {
            contentArea.addComponent(buildDashboard(), BorderLayout.Location.CENTER);
        } else {
            Panel p = new Panel(new LinearLayout(Direction.VERTICAL));
            p.addComponent(new EmptySpace(new TerminalSize(1, 4)));
            p.addComponent(new Label("=== " + page + " ===").setForegroundColor(TextColor.ANSI.GREEN));
            p.addComponent(new EmptySpace(new TerminalSize(1, 2)));
            p.addComponent(new Label("Module en cours de développement...").setForegroundColor(TextColor.ANSI.YELLOW));

            Panel centerHelper = new Panel(new GridLayout(1));
            centerHelper.addComponent(p,
                    GridLayout.createLayoutData(GridLayout.Alignment.CENTER, GridLayout.Alignment.CENTER, true, true));

            contentArea.addComponent(centerHelper, BorderLayout.Location.CENTER);
        }
        try {
            if (gui != null)
                gui.updateScreen();
        } catch (Exception e) {
        }
    }

    private static Component buildDashboard() {
        Panel dashboard = new Panel(new LinearLayout(Direction.VERTICAL));

        Panel kpiPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        dashboard.addComponent(kpiPanel);
        dashboard.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        // Progress bar for Rooms
        Panel progressPanel = new Panel(new BorderLayout());
        progressPanel.addComponent(new Label("Taux d'occupation des salles : "), BorderLayout.Location.LEFT);
        roomUsageBar = new ProgressBar(0, 100, 50);
        progressPanel.addComponent(roomUsageBar, BorderLayout.Location.CENTER);
        dashboard.addComponent(progressPanel.withBorder(Borders.singleLine(" Salles ")));
        dashboard.addComponent(new EmptySpace(new TerminalSize(1, 1)));

        // Middle section: Table and Alerts
        Panel midSection = new Panel(new BorderLayout());

        // Table
        Table<String> table = new Table<>("Nom", "Cours", "Groupe", "Date", "Statut");
        table.getTableModel().addRow("Jean Dupont", "POO Java", "Gr 1", "25/04", "Validé");
        table.getTableModel().addRow("Ada Lovelace", "Algo", "Gr 3", "23/04", "Refusé");
        table.getTableModel().addRow("Paul Dirac", "Maths", "Gr 1", "24/04", "En attente");
        table.getTableModel().addRow("Marie Curie", "Physique", "Gr 2", "24/04", "Validé");
        table.getTableModel().addRow("Alan Turing", "Systèmes", "Gr 2", "22/04", "Validé");

        table.setTableCellRenderer(new TableCellRenderer<String>() {
            @Override
            public TerminalSize getPreferredSize(Table<String> table, String cell, int columnIndex, int rowIndex) {
                return new TerminalSize(cell.length() + 2, 1);
            }

            @Override
            public void drawCell(Table<String> table, String cell, int columnIndex, int rowIndex,
                    TextGUIGraphics text) {
                ThemeDefinition theme = table.getTheme().getDefinition(Table.class);
                if (table.getSelectedRow() == rowIndex) {
                    text.applyThemeStyle(theme.getSelected());
                } else {
                    text.applyThemeStyle(theme.getNormal());
                }

                if (columnIndex == 4) {
                    if ("Validé".equals(cell))
                        text.setForegroundColor(TextColor.ANSI.GREEN);
                    else if ("En attente".equals(cell))
                        text.setForegroundColor(TextColor.ANSI.YELLOW);
                    else if ("Refusé".equals(cell))
                        text.setForegroundColor(TextColor.ANSI.RED);
                }
                text.putString(0, 0, cell);
            }
        });

        midSection.addComponent(table.withBorder(Borders.singleLine(" 5 dernières inscriptions ")),
                BorderLayout.Location.CENTER);

        // Alerts
        Panel alertsPanel = new Panel(new LinearLayout(Direction.VERTICAL));
        alertsPanel.addComponent(new Label("[!] Serveur BD lent").setForegroundColor(TextColor.ANSI.YELLOW));
        alertsPanel.addComponent(new Label("[X] Echec backup").setForegroundColor(TextColor.ANSI.RED));
        alertsPanel.addComponent(new Label("[i] Maintenance prévue").setForegroundColor(TextColor.ANSI.CYAN));

        midSection.addComponent(alertsPanel.withBorder(Borders.singleLine(" \u26A0 Alertes ")),
                BorderLayout.Location.RIGHT);

        dashboard.addComponent(midSection);

        // Animation thread
        new Thread(() -> {
            try {
                String[] titles = { "Étudiants \uD83C\uDF93", "Enseignants \uD83D\uDC68\u200D\uD83C\uDFEB",
                        "Cours Actifs \uD83D\uDCDA", "Groupes \uD83D\uDC65", "Salles \uD83C\uDFEB" };
                String[] values = { "1250", "89", "45", "12", "30" };
                String[] trends = { "\u2191", "\u2191", "\u2191", "\u2193", "\u2191" };

                for (int i = 0; i < 5; i++) {
                    Panel card = new Panel(new GridLayout(2));
                    card.addComponent(new Label(values[i]));
                    Label trend = new Label(trends[i]);
                    trend.setForegroundColor(trends[i].equals("\u2191") ? TextColor.ANSI.GREEN : TextColor.ANSI.RED);
                    card.addComponent(trend);

                    kpiPanel.addComponent(card.withBorder(Borders.singleLine(titles[i])));
                    gui.updateScreen();
                    Thread.sleep(200);
                }

                for (int i = 0; i <= 74; i += 2) {
                    roomUsageBar.setValue(i);
                    gui.updateScreen();
                    Thread.sleep(15);
                }
            } catch (Exception e) {
            }
        }).start();

        return dashboard;
    }
}
