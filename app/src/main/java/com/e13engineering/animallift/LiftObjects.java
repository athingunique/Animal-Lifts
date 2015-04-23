package com.e13engineering.animallift;

public class LiftObjects {
    public class LiftObject {
        String name, image;
        int lower, upper;
        private LiftObject(String name, int lower, int upper) {
            this.name = name;
            this.lower = lower;
            this.upper = upper;
            image = name.replace(" ","_").toLowerCase();
        }
    }

    // big string array with all the names and weights
    private String[] animalWeights = {
            "Chipotle Burrito", "0", "1",
            "Platypus", "1", "5",
            "European Hare", "5", "10",
            "Red Fox", "10", "15",
            "River Otter", "15", "20",
            "Wolverine", "20", "25",
            "Stately Iberian Lynx", "25", "30",
            "Baby Sea Lion", "30", "35",
            "Moose Calf", "35", "40",
            "Clouded Leopard", "40", "45",
            "Young Alpine Ibex", "45", "50",
            "Female Emperor Penguin", "50", "55",
            "Giant Armadillo", "55", "60",
            "Mature Wombat", "60", "65",
            "Newborn Manatee", "65", "70",
            "Small Mountain Goat", "70", "75",
            "Baby Zebra", "75", "80",
            "Newborn Texas Longhorn", "80", "85",
            "Spotted Serengeti Hyena", "85", "90",
            "Medium Warthog", "90", "95",
            "Cheetah", "95", "100",
            "Grey Wolf", "100", "110",
            "Young Male Puma", "110", "120",
            "Desert Bighorn Ewe", "120", "130",
            "Baby Giraffe", "130", "140",
            "Komodo Dragon", "140", "150",
            "Medium Sized Deer", "150", "160",
            "Mature Alpaca", "160", "170",
            "Male Red Kangaroo", "170", "180",
            "Black Bear", "180", "190",
            "Baby Elephant", "190", "200",
            "Fledgling Hippo", "200", "220",
            "Male Giant Panda", "220", "240",
            "Svelte Wildebeest", "240", "260",
            "Fledgling Pacific Walrus", "260", "280",
            "Loggerhead Sea Turtle", "280", "300",
            "Sloth Bear", "300", "320",
            "Mature Harbor Seal", "320", "340",
            "Newborn Orca Whale", "340", "360",
            "Black Jaguar", "360", "380",
            "Juvenile Crocodile", "380", "400",
            "West Indian Manatee", "400", "500",
            "Mature Bluefin Tuna", "500", "600",
            "Polar Bear", "500", "700",
            "Stately American Elk", "600", "700",
            "Superior Eastern Highland Gorilla", "700", "800",
            "Galapagos Tortoise", "800", "900",
            "Jersey Cow", "800", "1000",
            "Musk Ox", "900", "1000",
            "Hammerhead Shark", "900", "1000",
            "Mature Male Moose", "1000", "1800",
            "Clydesdale", "1800", "2300",
            "Water Buffalo", "2300", "3000",
            "Black Rhinoceros", "3000", "4000",
            "Newborn Blue Whale", "4000", "6001"};

    private LiftObject[] animals = new LiftObject[size()];

    private LiftObject get(int index) {
        if (index > size()-1) {
            return null;
        }
        return animals[index];
    }
    private int findIndex(int weight) {
        return binarySearch(weight);
    }

    public LiftObject[] getLift(int weight) {
        int i = findIndex(weight);
        if (i < 0) {
            return null;
        }
        LiftObject thisLift = get(i);
        LiftObject nextLift = get(i+1);
        return new LiftObject[] {thisLift,nextLift};
    }
    private int size() {
        return (animalWeights.length/3);
    }

    public LiftObjects() {
        for (int i = 0; i < size(); i++) {
            animals[i] = new LiftObject(
                    animalWeights[(i*3)],
                    Integer.parseInt(animalWeights[(i*3)+1]),
                    Integer.parseInt(animalWeights[(i*3)+2]));
        }
    }

    private int binarySearch(int val) {
        return search(val, 0, animals.length-1);
    }
    private int search(int val, int lo, int hi) {
        if (hi < lo) {
            return -1;
        }
        int mid = lo + (hi - lo)/2;
        int lower = animals[mid].lower;
        int upper = animals[mid].upper;

        if (lower <= val && val < upper) {
            return mid;
        } else if (val < lower) {
            return search(val, lo, mid);
        } else {
            return search(val, mid+1, hi);
        }
    }


}