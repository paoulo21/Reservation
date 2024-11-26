package Creneaux;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Constraints {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    public int[] enabledDays;
    public int maxPerSlot;
    public int minutesBetweenSlots;
    public LocalTime start;
    public LocalTime end;
    public String name;

    public Constraints(int[] enabled_days,int max_per_slot, int minutes_between_slots,LocalTime start,LocalTime end,String name){
        this.enabledDays = enabled_days;
        this.maxPerSlot = max_per_slot;
        this.minutesBetweenSlots = minutes_between_slots;
        this.start = start;
        this.end = end;
        this.name = name;

    }

    @Override
    public String toString() {
        return "Constraints [enabledDays=" + Arrays.toString(enabledDays) + ", maxPerSlot=" + maxPerSlot
                + ", minutesBetweenSlots=" + minutesBetweenSlots + ", start=" + start + ", end=" + end + ", name="
                + name + "]";
    }

    public int[] getEnabledDays() {
        return enabledDays;
    }

    public void setEnabledDays(int[] enabledDays) {
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

    
    
}
