package POJO;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.OrderColumn;

@Entity
@Table(name = "constraints_rules")
public class Constraints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Convert(converter = ListToStringConverter.class)
    @Column(name = "enabled_days", nullable = false)
    private List<Integer> enabledDays;

    public int maxPerSlot;
    public int minutesBetweenSlots;
    @Column(name = "startTime", columnDefinition = "TIME", nullable = false)
    public LocalTime start;
    @Column(name = "endTime", columnDefinition = "TIME", nullable = false)
    public LocalTime end;
    public String name;

    public Constraints(List<Integer> enabledDays, int maxPerSlot, int minutesBetweenSlots, LocalTime start, LocalTime end, String name) {
        this.enabledDays = enabledDays;
        this.maxPerSlot = maxPerSlot;
        this.minutesBetweenSlots = minutesBetweenSlots;
        this.start = start;
        this.end = end;
        this.name = name;
    }

    public Constraints(){
        
    }

    @Override
    public String toString() {
        return "Constraints [enabledDays=" + enabledDays + ", maxPerSlot=" + maxPerSlot
                + ", minutesBetweenSlots=" + minutesBetweenSlots + ", start=" + start + ", end=" + end + ", name=" 
                + name + "]";
    }

    

    public List<Integer> getEnabledDays() {
        return enabledDays;
    }

    public void setEnabledDays(List<Integer> enabledDays) {
        this.enabledDays = enabledDays;
    }

    public int getMaxPerSlot() {
        return maxPerSlot;
    }

    public void setMaxPerSlot(int maxPerSlot) {
        this.maxPerSlot = maxPerSlot;
    }

    public int getMinutesBetweenSlots() {
        return minutesBetweenSlots;
    }

    public void setMinutesBetweenSlots(int minutesBetweenSlots) {
        this.minutesBetweenSlots = minutesBetweenSlots;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
}
