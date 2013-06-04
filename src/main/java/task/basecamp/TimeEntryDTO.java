/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package task.basecamp;

import java.util.Date;

/**
 *
 * @author Hugo
 */
public class TimeEntryDTO {
    private long id;
    private Date date;
    private String description;
    private float hours;
    private long personId;
    private long projectId;
    private long todoItemId;
    private String personName;
    
    public TimeEntryDTO(){}
    
    public TimeEntryDTO(long personId, Date date, float hours, String desc){
        this.personId=personId;
        this.date=date;
        this.hours=hours;
        this.description=desc;
    }
    

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the hours
     */
    public float getHours() {
        return hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(float hours) {
        this.hours = hours;
    }

    /**
     * @return the personId
     */
    public long getPersonId() {
        return personId;
    }

    /**
     * @param personId the personId to set
     */
    public void setPersonId(long personId) {
        this.personId = personId;
    }

    /**
     * @return the projectId
     */
    public long getProjectId() {
        return projectId;
    }

    /**
     * @param projectId the projectId to set
     */
    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * @return the todoItemId
     */
    public long getTodoItemId() {
        return todoItemId;
    }

    /**
     * @param todoItemId the todoItemId to set
     */
    public void setTodoItemId(long todoItemId) {
        this.todoItemId = todoItemId;
    }

    /**
     * @return the personName
     */
    public String getPersonName() {
        return personName;
    }

    /**
     * @param personName the personName to set
     */
    public void setPersonName(String personName) {
        this.personName = personName;
    }
    
}
