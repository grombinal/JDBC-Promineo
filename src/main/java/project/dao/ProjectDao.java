 package project.dao;
 
import java.sql.*;
 
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import com.mysql.cj.protocol.Resultset;
import com.mysql.cj.x.protobuf.MysqlxPrepare.Prepare;

import project.entity.Project;
import project.exception.DbException;
import util.DaoBase;

public class ProjectDao extends DaoBase {

	 private static final String CATEGORY_TABLE = "category";
	 private static final String MATERIAL_TABLE = "material";
	 private static final String PROJECT_TABLE = "project";
	 private static final String PROJECT_CATEGORY_TABLE = "project_category";
	 private static final String STEP_TABLE = "step";
	
	
	
	
	public void executeBatch(List<String> sqlBatch) {
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			try (Statement stmt = conn.createStatement() ){
				for(String sql: sqlBatch) {
					stmt.addBatch(sql);
				}
				stmt.executeBatch();
				commitTransaction(conn);
			}
			catch(Exception e) {
				rollbackTransaction(conn);
				throw new DbException(e);
			}
		} catch (SQLException e) {
			throw new DbException(e);
		}
	} 

	public Project insertProject(Project project) {
		String sql = ""
				+"INSERT INTO " + PROJECT_TABLE + " "
				+"(project_name, estimated_hours, actual_hours, difficulty, notes) "
				+"VALUES "
				+"(?, ?, ?, ?, ?)";
		try(Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sql)){
				setParameter(stmt, 1, project.getProjectName(), String.class);
				setParameter(stmt, 2, project.getEstimatedHours(), BigDecimal.class);
				setParameter(stmt, 3, project.getActualHours(), BigDecimal.class);
				setParameter(stmt, 4, project.getDifficulty(), Integer.class);
				setParameter(stmt, 5, project.getNotes(), String.class);
				
				stmt.executeUpdate();
				
				Integer projectId = getLastInsertId(conn, PROJECT_TABLE);
				commitTransaction(conn);
				
				project.setProjectId(projectId);
				
				return project;
			}
			catch(Exception e){
				rollbackTransaction(conn);
				throw new DbException(e);			
				}							
		}catch(SQLException e) {
			throw new DbException(e);
		}
			
		
	}

	public List<Project> fetchAllProjects() {
		String sqlFecth = ""
				+" SELECT * FROM "+ PROJECT_TABLE  + " ORDER BY project_name";
		
		try (Connection conn = DbConnection.getConnection()){
			startTransaction(conn);
			
			try(PreparedStatement stmt = conn.prepareStatement(sqlFecth)){
				
				try(ResultSet rs = stmt.executeQuery()){
					
					List<Project> projects = new LinkedList<Project>();
					
					while (rs.next()) {
						projects.add(extract(rs, Project.class));
					}
					
					return projects;
				}
				
			}catch(Exception e){
				rollbackTransaction(conn);
				throw new DbException(e);
			}
			
		}catch(SQLException e) {
			throw new DbException(e);
			
		}
	}
}
