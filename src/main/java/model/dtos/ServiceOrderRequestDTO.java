package model.dtos;

import java.io.Serializable;

public class ServiceOrderRequestDTO implements Serializable {
    private String problemDescription;
    private String observations;

    public ServiceOrderRequestDTO() {
    }

    public ServiceOrderRequestDTO(String problemDescription, String observations) {
        this.problemDescription = problemDescription;
        this.observations = observations;
    }

    public String getProblemDescription() {
        return problemDescription;
    }

    public void setProblemDescription(String problemDescription) {
        this.problemDescription = problemDescription;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}
