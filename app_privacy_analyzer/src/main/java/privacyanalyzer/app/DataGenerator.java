package privacyanalyzer.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.vaadin.spring.annotation.SpringComponent;

import privacyanalyzer.backend.ApkRepository;

import privacyanalyzer.backend.PermissionRepository;

import privacyanalyzer.backend.ProtectionLevelRepository;
import privacyanalyzer.backend.TrackerRepository;
import privacyanalyzer.backend.UserRepository;
import privacyanalyzer.backend.VariablesRepository;

import privacyanalyzer.backend.data.Role;

import privacyanalyzer.backend.data.entity.Permission;


import privacyanalyzer.backend.data.entity.ProtectionLevel;
import privacyanalyzer.backend.data.entity.Tracker;
import privacyanalyzer.backend.data.entity.User;
import privacyanalyzer.backend.data.entity.Variables;

@SpringComponent
public class DataGenerator implements HasLogger {

	
	private final Random random = new Random(1L);

	
	
	private User baker;
	private User barista;

	@Bean
	public CommandLineRunner loadData(VariablesRepository variablesRepository, 
			UserRepository userRepository, 
			 ApkRepository apkRepository,
			PermissionRepository permissionRepository, ProtectionLevelRepository protectionLevelRepository,
			TrackerRepository trackerRepository, PasswordEncoder passwordEncoder) {
		return args -> {

			getLogger().info("Initialize database");

			if (!hasData(userRepository)) {
				getLogger().info("... generating users");
				createUsers(userRepository, passwordEncoder);
			} else {
				getLogger().info("Users exists");
			}
			/*
			 getLogger().info("... generating products");
			  createProducts(productRepository);
			  getLogger().info("... generating pickup locations");
			  createPickupLocations(pickupLocationRepository);
			  getLogger().info("... generating orders"); createOrders(orderRepository);
			 */
			// apkRepository.count();

			if (!hasData(protectionLevelRepository)) {
			getLogger().info("... loading protection levels");
			createProtectionLevels(protectionLevelRepository);
			} else {
				getLogger().info("protection levels exists");
			}
			
			if (!hasData(permissionRepository)) {
			getLogger().info("... loading permissions");
			createPermissions(permissionRepository, protectionLevelRepository);
			} else {
				getLogger().info("permissions exists");
			}
			
			
			if (!hasData(trackerRepository)) {
			getLogger().info("... loading trackers");
			createTrackers(trackerRepository);
			} else {
				getLogger().info("trackers exists");
			}
			
			if (!hasData(variablesRepository)) {
			getLogger().info("...generating variables");
			createVariables(variablesRepository);
			} else {
				getLogger().info("variables exists");
			}
			
			
			getLogger().info("Finished initializing database");
		};
	}

	private void createVariables(VariablesRepository variablesRepository) {
		Variables defaultVariables = new Variables();

		defaultVariables.setName("default");
		
		defaultVariables.setDeclaredAndNotUsedDangerousPermissionScore(3);
		defaultVariables.setDeclaredAndNotUsedSignatureSystemPermissionScore(1.5);

		defaultVariables.setDeclaredAndUsedDangerousPermissionScore(10);
		defaultVariables.setDeclaredAndUsedSignatureSystemPermissionScore(5);

		defaultVariables.setLibraryDangerousPermissionScore(5);
		defaultVariables.setLibrarySignatureSystemPermissionScore(2.5);

		defaultVariables.setNotDeclaredButUsedDangerousPermissionScore(15);
		defaultVariables.setNotDeclaredButUsedNormalPermissionScore(7.5);
		defaultVariables.setNotDeclaredButUsedSignatureSystemPermissionScore(3.75);

		defaultVariables.setAdbScore(3);
		defaultVariables.setDebuggableScore(8);
		defaultVariables.setMalwareScore(30);
		defaultVariables.setMaximumRiskScore(100);
		defaultVariables.setGreenRisk(25);
		defaultVariables.setOrangRisk(70);
		defaultVariables.setRedRisk(100);
		defaultVariables.setYellowRisk(40);
		defaultVariables.setVirusTotalScore(20);
		defaultVariables.setVirusTotalThreshold(0.3);
		variablesRepository.save(defaultVariables);

	}

	private void createTrackers(TrackerRepository trackerRepository) throws IOException {
		File file = new ClassPathResource("trackers.json").getFile();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(file));
		List<Tracker> data = gson.fromJson(reader, new TypeToken<List<Tracker>>() {
		}.getType());

		for (Tracker t : data) {
			trackerRepository.save(t);
		}

	}

	private void createProtectionLevels(ProtectionLevelRepository protectionLevelRepository) throws IOException {
		File file = new ClassPathResource("protectionlevels.json").getFile();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(file));
		List<ProtectionLevel> data = gson.fromJson(reader, new TypeToken<List<ProtectionLevel>>() {
		}.getType());

		for (ProtectionLevel p : data) {
			protectionLevelRepository.save(p);
		}

	}

	private void createPermissions(PermissionRepository permissionRepository,
			ProtectionLevelRepository protectionLevelRepository) throws IOException {
		File file = new ClassPathResource("permissions.json").getFile();
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(file));
		List<Permission> data = gson.fromJson(reader, new TypeToken<List<Permission>>() {
		}.getType());

		for (Permission p : data) {
			p.setProtectionlvl(protectionLevelRepository.findByName(p.getProtectionLevel()));
			permissionRepository.save(p);
		}

	}

	private boolean hasData(JpaRepository repo) {
		return repo.count() != 0L;
	}

	

	private String getRandomPhone() {
		return "+1-555-" + String.format("%04d", random.nextInt(10000));
	}

	

	

	private LocalTime getRandomDueTime() {
		int time = 8 + 4 * random.nextInt(3);

		return LocalTime.of(time, 0);
	}

	

	private User getBaker() {
		return baker;
	}

	private User getBarista() {
		return barista;
	}

	private <T> T getRandom(List<T> items) {
		return items.get(random.nextInt(items.size()));
	}

	private <T> T getRandom(T[] array) {
		return array[random.nextInt(array.length)];
	}

	

	private void createUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		// to be replaced with actual users
		baker = userRepository
				.save(new User("user1@vaadin.com", "null", null, Role.USER));
		User user = new User("user@vaadin.com", "null", null, Role.USER);
		user.setLocked(true);
		barista = userRepository.save(user);
		user = new User("admin@admin.com", "Admin", null, Role.ADMIN);
		user.setLocked(true);
		userRepository.save(user);
		user = new User("guest", "guest", null, Role.GUEST);
		user.setLocked(true);
		userRepository.save(user);
	}
}
