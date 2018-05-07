package kr.co.company.ProjectA;

import java.util.Comparator;

public class PharmacyArraySort implements Comparator<Pharmacy> {
    @Override
    public int compare(Pharmacy first, Pharmacy second) {
        int firstValue = first.between_dis;
        int secondValue = second.between_dis;
        if (firstValue > secondValue) {
            return 1;
        }
        else if (firstValue < secondValue) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
