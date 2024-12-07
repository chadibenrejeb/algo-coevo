package ALGO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class EnhancedPredatorPraySimulation extends JFrame {
    private CoEvolutionSimulation simulation;
    private int generation;

    // UI Components
    private JLabel generationLabel;
    private JTable predatorTable;
    private JTable preyTable;
    private JPanel statsPanel;
    private JButton evolveButton;
    private JButton resetButton;

    public EnhancedPredatorPraySimulation() {
        // Initialize simulation
        simulation = new CoEvolutionSimulation();
        generation = 0;

        // Setup frame
        setTitle("Predator-Prey Co-Evolution Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(40, 44, 52)); // Dark background

        // Create and add components
        createTopPanel();
        createCenterPanel();
        createBottomPanel();

        // Initial population display
        updatePopulationDisplay();
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(40, 44, 52));

        JLabel titleLabel = new JLabel("Predator-Prey Co-Evolution Simulator");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

        generationLabel = new JLabel("Generation: 0");
        generationLabel.setForeground(Color.LIGHT_GRAY);
        generationLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        topPanel.add(titleLabel);
        topPanel.add(generationLabel);
        add(topPanel, BorderLayout.NORTH);
    }

    private void createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setBackground(new Color(40, 44, 52));

        // Predator Table
        predatorTable = createStyledTable("Predators");
        JScrollPane predatorScrollPane = new JScrollPane(predatorTable);
        predatorScrollPane.setBorder(BorderFactory.createTitledBorder("Predator Population"));
        predatorScrollPane.getViewport().setBackground(new Color(50, 54, 62));

        // Prey Table
        preyTable = createStyledTable("Prey");
        JScrollPane preyScrollPane = new JScrollPane(preyTable);
        preyScrollPane.setBorder(BorderFactory.createTitledBorder("Prey Population"));
        preyScrollPane.getViewport().setBackground(new Color(50, 54, 62));

        centerPanel.add(predatorScrollPane);
        centerPanel.add(preyScrollPane);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JTable createStyledTable(String type) {
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"ID", "Speed"}, 0
        );
        JTable table = new JTable(model);
        
        // Styling
        table.setBackground(new Color(50, 54, 62));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(70, 74, 82));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(new Color(70, 74, 82));
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setBackground(new Color(40, 44, 52));
        table.getTableHeader().setForeground(Color.WHITE);
        
        return table;
    }

    private void createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(40, 44, 52));

        // Evolve Button
        evolveButton = createStyledButton("Simulate Generation");
        evolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simulateGeneration();
            }
        });

        // Reset Button
        resetButton = createStyledButton("Reset Simulation");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetSimulation();
            }
        });

        bottomPanel.add(evolveButton);
        bottomPanel.add(resetButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(70, 74, 82));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void updatePopulationDisplay() {
        // Update Predator Table
        DefaultTableModel predatorModel = (DefaultTableModel) predatorTable.getModel();
        predatorModel.setRowCount(0);
        for (int i = 0; i < simulation.predateurs.size(); i++) {
            Prédateur predator = simulation.predateurs.get(i);
            predatorModel.addRow(new Object[]{
                i + 1, 
                predator.vitesse
            });
        }

        // Update Prey Table
        DefaultTableModel preyModel = (DefaultTableModel) preyTable.getModel();
        preyModel.setRowCount(0);
        for (int i = 0; i < simulation.proies.size(); i++) {
            Proie prey = simulation.proies.get(i);
            preyModel.addRow(new Object[]{
                i + 1, 
                prey.vitesse
            });
        }

        // Update Generation Label
        generationLabel.setText("Generation: " + generation);
    }

    private void simulateGeneration() {
        generation++;
        simulation.simulerGeneration();
        updatePopulationDisplay();
    }

    private void resetSimulation() {
        generation = 0;
        simulation.initialiserPopulations();
        updatePopulationDisplay();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EnhancedPredatorPraySimulation frame = new EnhancedPredatorPraySimulation();
            frame.setVisible(true);
        });
    }

    // Inner classes from the original simulation (Proie, Prédateur, CoEvolutionSimulation)
    // These remain exactly the same as in the original code
    static class Proie {
        int vitesse;

        Proie(int vitesse) {
            this.vitesse = vitesse;
        }

        void evoluer() {
            this.vitesse += new Random().nextInt(3) + 1;
        }
    }

    static class Prédateur {
        int vitesse;

        Prédateur(int vitesse) {
            this.vitesse = vitesse;
        }

        void evoluer() {
            this.vitesse += new Random().nextInt(3) + 1;
        }

        boolean capture(Proie proie) {
            return this.vitesse > proie.vitesse;
        }
    }

    static class CoEvolutionSimulation {
        ArrayList<Prédateur> predateurs;
        ArrayList<Proie> proies;
        Random rand = new Random();

        CoEvolutionSimulation() {
            predateurs = new ArrayList<>();
            proies = new ArrayList<>();
            initialiserPopulations();
        }

        void initialiserPopulations() {
            predateurs.clear();
            proies.clear();
            for (int i = 0; i < 5; i++) {
                predateurs.add(new Prédateur(rand.nextInt(20) + 1));
                proies.add(new Proie(rand.nextInt(20) + 1));
            }
        }

        void simulerGeneration() {
            for (int i = 0; i < predateurs.size(); i++) {
                Prédateur predateur = predateurs.get(i);
                Proie proie = proies.get(i);

                if (predateur.capture(proie)) {
                    proie.evoluer();
                } else {
                    predateur.evoluer();
                }
            }
        }
    }
}
