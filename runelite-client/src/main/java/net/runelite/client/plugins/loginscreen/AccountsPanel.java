package net.runelite.client.plugins.loginscreen;

import com.google.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

class AccountsPanel extends PluginPanel
{
	private final String accountsFileDir = System.getProperty("user.home") + "\\.runelite\\accounts.txt";

	private Client client;
	private LoginScreenPlugin plugin;

	private JPanel accountsContainer;

	private Map<String, String[]> accounts = new TreeMap<>();

	@Inject
	private AccountsPanel(Client client, LoginScreenPlugin plugin)
	{
		this.client = client;
		this.plugin = plugin;

		setBackground(ColorScheme.DARK_GRAY_COLOR);

		createPanel();
	}

	private void createPanel()
	{
		add(accountsContainer = createAccountsContainer());

		populateAccountsContainer();
	}

	private void populateAccountsContainer()
	{
		accountsContainer.removeAll();

		readAccounts();

		accounts.keySet().forEach(account ->
		{
			final String[] credentials = accounts.get(account);
			accountsContainer.add(createAccountPanel(account, credentials[0], credentials[1]));
		});

		revalidate();
		repaint();
	}

	// Create the container panel for the accounts
	private JPanel createAccountsContainer()
	{
		final JPanel container = new JPanel();
		container.setLayout(new GridLayout(2, 1));
		return container;
	}

	// Create the panel for each account
	private JPanel createAccountPanel(String name, String user, String pass)
	{
		final JPanel container = new JPanel();
		container.setBackground(ColorScheme.DARK_GRAY_COLOR);

		final JLabel label = new JLabel(name);
		final JButton setButton = new JButton("Login");

		setButton.addActionListener((ev) ->
		{
			client.setUsername(user);
			client.setPassword(pass);
		});

		container.add(label);
		container.add(setButton);

		return container;
	}

	private void readAccounts()
	{
		final BufferedReader reader;

		try
		{
			final File accountsFile = new File(accountsFileDir);
			accountsFile.createNewFile();
			reader = new BufferedReader(new FileReader(accountsFile));
			String line = reader.readLine();

			while (line != null)
			{
				final String[] account = line.split(":");
				accounts.put(account[0], new String[] { account[1], account[2] });
				line = reader.readLine();
			}

			reader.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
