package project.service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import project.dao.ProjectDao;
import project.entity.Project;
import project.exception.DbException;

public class ProjectService {
	
	@SuppressWarnings("unused")
	private static final String SCHEMA_FILE = "project_schema.sql";
	
	private ProjectDao  projectDao = new ProjectDao();
	
	public void createAndPopulateTables() {
		loadFromFile(SCHEMA_FILE);
		
	}

	private void loadFromFile(String fileName) {
		String content = readFileContent(fileName);
		List<String> sqlstatements = convertContentToSqlStatements(content);
		// this prints the whole Query 
		//sqlstatements.forEach(line -> System.out.println(line));
		
		projectDao.executeBatch(sqlstatements);
	}

	private List<String> convertContentToSqlStatements(String content) {
		content = removeComments(content);
		content = replaceWhitespaceWithSingleSpace(content);
		
		return extractLinesFromContent(content);
	}

	private List<String> extractLinesFromContent(String content) {
		List<String> lines = new LinkedList<>();
		
		while(!content.isEmpty()){
			int semicolon = content.indexOf(";");
			
			if(semicolon == -1){
				if(!content.isBlank()) {
				lines.add(content);}
				content = "";
			}
			
			else {
				lines.add(content.substring(0, semicolon).trim());
				content = content.substring( semicolon +1);
			}
		}
		
		
		return lines;
	}

	private String replaceWhitespaceWithSingleSpace(String content) {
	return content.replaceAll("\\s+"," ");
	}

	private String removeComments(String content) {
	StringBuilder builder = new StringBuilder (content);
	String a = "";
	//Use this variable to know our position 
	int commentpos = 0;
	while ((commentpos = builder.indexOf("-- ",commentpos)) != -1) {
		int eolPos = builder.indexOf("\n", commentpos +1);
		
		if (eolPos == -1) {
			builder.replace(commentpos, builder.length(), "" );
		}else {
			builder.replace(commentpos, eolPos +1, "" );
		}
		
	}
	return builder.toString();//--> this was used to return the SQL final Statement that was used to create the tables
	}

	private String readFileContent(String fileName) {
		
	try {
		Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());
		return Files.readString(path);
	} catch (Exception e) {
		throw new DbException(e);
	}
	}
	public static void main (String[] args) {
		new ProjectService().createAndPopulateTables();
	}

	public Project addProject(Project project) {
			
		return projectDao.insertProject(project);
	}
}

