import java.io.*;
import java.util.Scanner;
import java.util.Random;

public class Simple_RPG_build_2 {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m"; //damage taken
    public static final String GREEN = "\u001B[32m"; //room messages
    public static final String YELLOW = "\u001B[33m";//loot grantings
    public static final String BLUE = "\u001B[34m";//
    public static final String PURPLE = "\u001B[35m";//
    public static final String CYAN = "\u001B[36m";//announcments
    public static final String WHITE = "\u001B[37m";//

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        //FILE INITIALIZER

        int roommax = 20;
        int hp = 130;
        int acc = 90;
        int dmg = 15;
        int loot = 0;
        int crit = 0;
        int discount = 0;
        int randomize = 0;
        int roomnumber = 1;
        int loottotal = 0;
        int hpenemy = 0;
        int accenemy = 0;
        int dmgenemy = 0;
        int critmod = 0;
        int lastdiff = 8;
        boolean gamestarted = false;
        int[] details = {0};
        boolean[] granted = {false, false, false};
        int boughtmagic = 0;
        int dmgred = 0;
        String weapon = "none";
        boolean boss = false;
        while (!gamestarted) {
            System.out.println(GREEN + "==============================");
            System.out.println(GREEN + "=============================");
            System.out.println(GREEN + "===WELCOME TROLL WARRIOR!====");
            System.out.println(GREEN + "=============================");
            System.out.println(GREEN + "=============================" + RESET);
            System.out.println(" ");
            System.out.println(CYAN+"You are a simple minded troll who wants to prove their worth");
            System.out.println("and secure as much loot as possible, by defeating enemies.");
            System.out.println("You will face many foes, some that are too strong for you.");
            System.out.println("But you can use loot to power up until you are the TROLL KING." + '\n'+RESET);
            String career = "None";
            //System.out.println("Your starting stats: ");
            //System.out.println("HP: " + hp);
            //System.out.println("accuracy: " + acc + "%");
            //System.out.println("damage: " + dmg);
            //System.out.println("crit: " + crit);
            //System.out.println("discount: " + discount);
            details = chooseclass(career, hp, acc, dmg, discount, crit, loot, dmgred);
            hp = details[0];
            acc = details[1];
            dmg = details[2];
            discount = details[3];
            crit = details[4];
            loot = details[5];
            dmgred = details[6];
            //System.out.println(BLUE + "You have chosen career: " + career + RESET);
            System.out.println('\n' + "The boss will appear at room " + 20 + '\n' + RESET);
            System.out.println("Your new stats: ");
            System.out.println(BLUE+"HP " + hp);
            System.out.println(GREEN+"accuracy: " + acc + "%");
            System.out.println(PURPLE+"dmg: " + dmg);
            System.out.println("");
            System.out.println(CYAN + "type help for commandlist" + RESET);
            gamestarted = true;
            System.out.println(" ");
        } // game initializer

        int terrint = random.nextInt(2)+1;
        terrint = 1;
        String terrainstring = Integer.toString(terrint);
        String terrain2 = terrainstring.concat("0.txt");
        String terrain = terrainstring.concat(".txt");


        while (roomnumber < roommax && hp > 0) {

            while (hp > 0 && roomnumber <= (0.5 * roommax)) {
                System.out.println(CYAN + "You have entered room number: " + roomnumber + '\n' + RESET);
                roomnumber++;

                String filename = "Rooms.txt";
                randomize = getroomdesc(randomize, filename);
                //System.out.println(randomize);
                int accmod = randomize * 3;
                int acctot = acc - accmod;
                //int goldmod = randomize * 10;

                int enemydiff = 0;
                filename = terrain;
                enemydiff = getenemydesc(enemydiff, filename, lastdiff);
                lastdiff = enemydiff;

                //System.out.println(enemydiff);

                hpenemy = (enemydiff) * 12 + 5; //keep base int value for hpenemy or shit will break sometimes
                accenemy = (enemydiff) * 2 + 50;
                dmgenemy = (2*(enemydiff) + 2);
                int output = 0;
                boolean sameroom = true;
                while (sameroom) {
                    while (output == 0) {

                        String task = inputcommand();

                        output = processcommand(task, output, hpenemy, accenemy, dmgenemy, hp, acctot, dmg, loot, discount, crit, granted, dmgred, boss);

                    } //process changes output to 1 or 2 or 3
                    if (output == 1) { //leave
                        sameroom = false;
                    } //leave
                    if (output == 3) {
                        int boughtmagicold = boughtmagic;
                        details = openshop(loot, hp, acc, dmg, discount, crit, granted, boughtmagic);
                        loot = details[0];
                        hp = details[1];
                        acc = details[2];
                        acctot = acc - accmod;
                        dmg = details[3];
                        crit = details[4];
                        boughtmagic = details[5];
                        if (boughtmagic > boughtmagicold) {
                            int[] details1 = resolvemagicitem(granted, dmgred, roomnumber, critmod);
                            dmgred = details1[0];
                            roomnumber = details1[1];
                            critmod = details1[2];
                        } // DONT TYPE IN DETAILS ISNTEAD OF DETAILS1 DUMBASS
                        sameroom = true;
                        output = 0;
                    } //upgrade
                    if (output == 2) { //attack
                        if (hp > 0 && hpenemy > 0) {
                            int hpold = hp;
                            int hpenemyold = hpenemy;

                            while (!weapon.equals("d") && !weapon.equals("b") && !weapon.equals("s") && !weapon.equals("a")) {
                                System.out.println("Which weapon do you choose? " + CYAN + "Dagger (++crit, -dmg),"+YELLOW+" Bow (+acc, -dmg, +dmgred),"+BLUE+" Sword (+dmg),"+RED+" Axe (++dmg, +crit, --acc)" + RESET);
                                weapon = sc.nextLine();
                                weapon = weapon.toLowerCase();
                                if (!weapon.equals("")) {
                                    weapon = weapon.substring(0, 1);
                                }
                            }
                            hpenemy = playerattack(hpenemy, accenemy, dmgenemy, hp, acctot, dmg, crit, weapon, critmod);
                            hp = enemyattack(hpenemy, accenemy, dmgenemy, hp, acctot, dmg, dmgred, weapon);
                            weapon = "none";

                            if (hpold > hp) {
                                int damage = hpold - hp;
                                System.out.println(BLUE + "You took " + damage + " damage!" + RESET);
                                System.out.println(" ");
                                System.out.println(BLUE + "==================You have " + hp + " hp left=================" + RESET);
                            } else {
                                System.out.println(CYAN + "The enemy missed their attack!" + RESET);
                            }
                            System.out.println(" ");
                            if (hpenemyold > hpenemy) {
                                int damageenemytook = hpenemyold - hpenemy;
                                System.out.println(RED + "The enemy took " + damageenemytook + " damage");
                                System.out.println(" ");
                                System.out.println(RED + "The enemy has " + hpenemy + " hp left." + RESET);
                            } else if (hpenemyold == hpenemy) {
                                System.out.println(PURPLE+"You have missed your attack!"+RESET);
                            } else if (hpenemyold < hpenemy) {
                                System.out.println(PURPLE+"You have HEALED the enemy!!!"+RESET);
                            }
                            System.out.println(" ");
                            if (hpenemy <= 0) {
                                System.out.println(CYAN+"The enemy died."+RESET);
                                System.out.println(" ");
                                int lootadded = grantloot(enemydiff);
                                loot = loot + lootadded;
                                loottotal = lootadded + loottotal;
                                System.out.println(YELLOW + "You earned " + lootadded + " loot." + RESET);
                                System.out.println(CYAN+"You have " + loot + " loot and " + hp + " hp."+RESET);
                            }
                            if (hp <= 0) {
                                System.out.println(RED + '\n' + "==============================");
                                System.out.println(RED + "=============================");
                                System.out.println(RED + "===YOU PERISHED HORRIBLY=====");
                                System.out.println(RED + "=============================");
                                System.out.println(RED + "=============================" + '\n' + RESET);
                                System.out.println(CYAN+"You managed to gather a total of " + loottotal + " loot and made it to room number " + (roomnumber - 1));
                                sameroom = false;
                            }
                            output = 0;
                        }
                        output = 0;
                        sameroom = true;
                    }  //combat
                } //normal game loop
            } //room cycle 1

            if (hp > 0){
                System.out.println(" ");
                System.out.println(CYAN+"=============================================================");
                System.out.println("You find a staircase that descends deeper into the dungeon...");
                System.out.println("============================================================="+RESET);
                System.out.println(" ");
                System.out.println("The enemies here will be harder");
                System.out.println(" ");
                //System.out.println("roommax = "+roommax+"and roomnumber = "+roomnumber);
            } //staircase text

            while (hp > 0 && roomnumber < roommax) {

                System.out.println(CYAN + "You have entered room number: " + roomnumber + '\n' + RESET);
                String filename = "Rooms.txt";
                randomize = getroomdesc(randomize, filename);
                //System.out.println(randomize);
                roomnumber++;
                int accmod = randomize * 5;
                int acctot = acc - accmod;
                //int goldmod = randomize * 10;
                int enemydiff = 0;
                filename = terrain2;
                enemydiff = getenemydesc(enemydiff, filename, lastdiff);
                lastdiff = enemydiff;
                enemydiff = enemydiff + 8;
                hpenemy = (enemydiff) * 12 + 5; //keep base int value for hpenemy or shit will break sometimes
                accenemy = (enemydiff) * 2 + 50;
                dmgenemy = ((enemydiff) + 5);
                int output = 0;
                boolean sameroom = true;
                while (sameroom) {
                    while (output == 0) {
                        String task = inputcommand();

                        output = processcommand(task, output, hpenemy, accenemy, dmgenemy, hp, acctot, dmg, loot, discount, crit, granted, dmgred, boss);

                    }
                    if (output == 1) { //leave
                        sameroom = false;
                    } //leave
                    if (output == 3) {
                        int boughtmagicold = boughtmagic;
                        details = openshop(loot, hp, acc, dmg, discount, crit, granted, boughtmagic);
                        loot = details[0];
                        hp = details[1];
                        acc = details[2];
                        acctot = acc - accmod;
                        dmg = details[3];
                        crit = details[4];
                        boughtmagic = details[5];
                        if (boughtmagic > boughtmagicold) {
                            int[] details1 = resolvemagicitem(granted, dmgred, roomnumber, critmod);
                            dmgred = details1[0];
                            roomnumber = details1[1];
                            critmod = details1[2];
                        } // DONT TYPE IN DETAILS ISNTEAD OF DETAILS1 DUMBASS
                        sameroom = true;
                        output = 0;
                    } //upgrade
                    if (output == 2) { //attack
                        output = 0;
                        if (hp > 0 && hpenemy > 0) {
                            //System.out.println("hp > 0 and hpenemy >0");
                            int hpold = hp;
                            int hpenemyold = hpenemy;
                            while (!weapon.equals("d") && !weapon.equals("b") && !weapon.equals("s") && !weapon.equals("a")) {
                                System.out.println("Which weapon do you choose? " + CYAN + "Dagger (++crit, -dmg),"+YELLOW+" Bow (+acc, -dmg, +dmgred),"+BLUE+" Sword (+dmg),"+RED+" Axe (++dmg, +crit, --acc)" + RESET);
                                weapon = sc.nextLine();
                                weapon = weapon.toLowerCase();
                                if (!weapon.equals("")) {
                                    weapon = weapon.substring(0, 1);
                                }
                            }
                            hpenemy = playerattack(hpenemy, accenemy, dmgenemy, hp, acctot, dmg, crit, weapon, critmod);
                            hp = enemyattack(hpenemy, accenemy, dmgenemy, hp, acctot, dmg, dmgred, weapon);
                            weapon = "none";

                            if (hpold > hp) {
                                int damage = hpold - hp;
                                System.out.println(BLUE + "You took " + damage + " damage!" + RESET);
                                System.out.println(" ");
                                System.out.println(BLUE + "============You have " + hp + " hp left=========" + GREEN);
                            } else {
                                System.out.println(CYAN + "The enemy missed their attack!" + RESET);
                            }
                            System.out.println(" ");
                            if (hpenemyold > hpenemy) {
                                int damageenemytook = hpenemyold - hpenemy;
                                System.out.println(RED + "The enemy took " + damageenemytook + " damage");
                                System.out.println(" ");
                                System.out.println(RED + "The enemy has " + hpenemy + " hp left." + RESET);
                            } else if (hpenemyold == hpenemy) {
                                System.out.println(PURPLE+"You have missed your attack!"+RESET);
                            } else if (hpenemyold < hpenemy) {
                                System.out.println(PURPLE+"You have HEALED the enemy!!!"+RESET);
                            }
                            System.out.println(" ");
                            if (hpenemy <= 0) {
                                System.out.println(RED + "The enemy died."+RESET);
                                System.out.println(" ");
                                int lootadded = grantloot(enemydiff);
                                loot = loot + lootadded;
                                loottotal = lootadded + loottotal;
                                System.out.println(YELLOW + "You earned " + lootadded + " loot." + RESET);
                                System.out.println(CYAN+"You have " + loot + " loot and " + hp + " hp.");
                                sameroom = true;
                            }
                            if (hp <= 0) {
                                System.out.println(RED + '\n' + "==============================");
                                System.out.println(RED + "=============================");
                                System.out.println(RED + "===YOU PERISHED HORRIBLY=====");
                                System.out.println(RED + "=============================");
                                System.out.println(RED + "=============================" + '\n' + RESET);
                                System.out.println("You managed to gather a total of " + loottotal + " loot and made it to room number " + (roomnumber - 1));
                                sameroom = false;
                            }
                            output = 0;
                        }
                    }
                } //normal game loop
            } //room cycle 2


        } // rooms

        if (roomnumber >= roommax && hp > 0) {
            boss = true; //this variable does nothing but im scared to delete it
            hpenemy = 1;
            boolean displayed = false; //half way messages
            if (terrint == 1){
                hpenemy = 600;
                accenemy = 70;
                dmgenemy = 40;
                System.out.println(" "+RESET);
                System.out.println("==============================================================================================================="+RESET);
                System.out.println(" "+RESET);
                System.out.println(CYAN+"You enter a large circular room with immense pillars, flooded to your ankles with a putrid, crimson liquid.");
                System.out.println("A man with a golden crown and tattered robes sits kneeling in the center of the room, his sobbing echoing quietly ");
                System.out.println("throughout the chamber. He lets out a cry of pain and anger as he begins to transform. His face turns twisted ");
                System.out.println("and bestial while his limbs extend outwards, growing huge claws. He howls outwards, and prepares to feast.");
                System.out.println(" "+RESET);
                System.out.println("...You either die a hero, or live long enough to become something else...");
                System.out.println(" "+RESET);
                System.out.println("===============================================================================================================");
                System.out.println(" ");
                System.out.println("YOU FACE THE "+YELLOW+"FERAL KING"+RESET+"!");
                System.out.println(" ");
            } //boss intro 1
            if (terrint == 2){ //boss intro 2
                hpenemy = 500;
                accenemy = 85;
                dmgenemy = 60;
                acc = acc - 80;
                System.out.println(" "+RESET);
                System.out.println("======================================================================================"+RESET);
                System.out.println(" "+RESET);
                System.out.println(CYAN+"This large yet simple room is incredibly dark. You light a torch to illuminate the area");
                System.out.println("but a cold gust of wind blows the torch out. As you peer ahead into the inky void you");
                System.out.println("feel a presence. Before you appears a misty shadow in the vague form of a human. This ");
                System.out.println("illusory being is death given life, a being a pure malice and hatred.  ");
                System.out.println(" "+RESET);
                System.out.println("Before there was time--before there was anything-- there was nothing. And before there was nothing, there were monsters.");
                System.out.println(" ");
                System.out.println("======================================================================================");
                System.out.println(" ");
                System.out.println("YOU FACE THE "+PURPLE+"WRAITH"+RESET+"!");
                System.out.println(" ");
            }
            if (terrint == 3){

            } // boss intro 3

            while (hp > 0 && hpenemy > 0) {
                int output = 0;

                while (output == 0) {
                    String task = inputcommand();
                    output = processcommand(task, output, hpenemy, accenemy, dmgenemy, hp, acc, dmg, loot, discount, crit, granted, dmgred, boss);
                    if (output == 1) {
                        System.out.println(CYAN + "You cannot run from this fight!" + RESET);
                        output = 0;
                    }
                } //process commands

                if (output == 3) {
                    int boughtmagicold = boughtmagic;
                    details = openshop(loot, hp, acc, dmg, discount, crit, granted, boughtmagic);
                    if (boughtmagic > boughtmagicold) {
                        int[] details1 = resolvemagicitem(granted, dmgred, roomnumber, critmod);
                        dmgred = details1[0];
                        roomnumber = details1[1];
                        critmod = details1[2];
                    }
                    loot = details[0];
                    hp = details[1];
                    acc = details[2];
                    acc = acc;
                    dmg = details[3];
                    crit = details[4];
                    boughtmagic = details[5];
                    output = 0;
                } //upgrade

                if (output == 2 && terrint == 1) {//feral king (multihit)
                    int hpenemymax = 400;
                    int hpold = hp;
                    int hpenemyold = hpenemy;
                    weapon = "none";

                    while (!weapon.equals("d") && !weapon.equals("b") && !weapon.equals("s") && !weapon.equals("a")) {
                        System.out.println("Which weapon do you choose? " + CYAN + "Dagger (++crit, -dmg),"+YELLOW+" Bow (+acc, -dmg, +dmgred),"+BLUE+" Sword (+dmg),"+RED+" Axe (++dmg, +crit, --acc)" + RESET);
                        weapon = sc.nextLine();
                        weapon = weapon.toLowerCase();
                        if (!weapon.equals("")) {
                            weapon = weapon.substring(0, 1);
                        }
                    }
                    hpenemy = playerattack(hpenemy, accenemy, dmgenemy, hp, acc, dmg, crit, weapon, critmod);
                    hp = enemyattack(hpenemy, accenemy, dmgenemy, hp, acc, dmg, dmgred, weapon);

                    if (hpold > hp) {
                        int damage = hpold - hp;
                        System.out.println(RED + "The "+YELLOW+"FERAL KING "+RED +"dealt "+ damage + " damage to you!" + RESET);
                    } else {
                        int randommsg = random.nextInt(3);
                        if (randommsg == 0){
                            System.out.println(BLUE + "You barely dodge the "+YELLOW+"FERAL KINGs"+BLUE+" first attack!" + RESET);
                        }
                        if (randommsg == 1){
                            System.out.println(BLUE + "You manage to block the "+YELLOW+"FERAL KINGs"+BLUE+" first attack!" + RESET);
                        }
                        if (randommsg == 2){
                            System.out.println(BLUE + "You duck below the "+YELLOW+"FERAL KINGs"+BLUE+" first attack!" + RESET);
                        }
                    } //miss messages

                    System.out.println();
                    hpold = hp;
                    hp = enemyattack(hpenemy, accenemy, dmgenemy, hp, acc, dmg, dmgred, weapon);

                    if (hpold > hp) {
                        int damage = hpold - hp;
                        System.out.println(RED + "The "+YELLOW+"FERAL KING "+RED +"dealt "+ damage + " damage to you!" + RESET);
                    } else {
                        int randommsg = random.nextInt(3);
                        if (randommsg == 0){
                            System.out.println(BLUE + "You barely dodge the "+YELLOW+"FERAL KINGs"+BLUE+" second attack!" + RESET);
                        }
                        if (randommsg == 1){
                            System.out.println(BLUE + "You manage to block the "+YELLOW+"FERAL KINGs"+BLUE+" second attack!" + RESET);
                        }
                        if (randommsg == 2){
                            System.out.println(BLUE + "You duck below the "+YELLOW+"FERAL KINGs"+BLUE+" second swipe!" + RESET);
                        }
                    } //miss messages
                    System.out.println(" ");
                    System.out.println(RED + "=============You have " + hp + " hp left!============");
                    System.out.println(" ");

                    if (hpenemyold > hpenemy) {
                        int damageenemytook = hpenemyold - hpenemy;
                        System.out.println(BLUE + "The "+YELLOW+"FERAL KING"+BLUE+" took " + damageenemytook + " damage"+RESET);
                        if (hpenemy <= hpenemymax/2 && displayed == false){
                            System.out.println("The "+YELLOW+"FERAL KING"+RESET+" is noticeably injured!");
                            displayed = true;
                        } //1/2 way message
                    } else {
                        System.out.println(RED+"The "+YELLOW+"FERAL KING "+RED+"blocked your attack!"+RESET);
                    } //enemy dodge msgs
                    System.out.println(" ");

                } // boss fight 1

                if (output == 2 && terrint == 2) {
                    int hpenemymax = 300;
                    int hpold = hp;
                    int hpenemyold = hpenemy;
                    weapon = "none";

                    while (!weapon.equals("d") && !weapon.equals("b") && !weapon.equals("s") && !weapon.equals("a")) {
                        System.out.println("Which weapon do you choose? " + CYAN + "Dagger (++crit, -dmg),"+YELLOW+" Bow (+acc, -dmg, +dmgred),"+BLUE+" Sword (+dmg),"+RED+" Axe (++dmg, +crit, --acc)" + RESET);
                        weapon = sc.nextLine();
                        weapon = weapon.toLowerCase();
                        if (!weapon.equals("")) {
                            weapon = weapon.substring(0, 1);
                        }
                    }

                    hpenemy = playerattack(hpenemy, accenemy, dmgenemy, hp, acc, dmg, crit, weapon, critmod);

                    hp = enemyattack(hpenemy, accenemy, dmgenemy, hp, acc, dmg, dmgred, weapon);

                    if (hpold > hp) {
                        int damage = hpold - hp;
                        System.out.println(RED + "The "+PURPLE+"WRAITH "+RED +"dealt "+ damage + " damage to you!" + RESET);
                    } else {
                        System.out.println(BLUE + "You barely avoid the "+PURPLE+"WRAITHs "+BLUE+"shadowy assault!"+RESET);
                    }
                    //miss messages

                    System.out.println(" ");
                    System.out.println(RED + "You have " + hp + " hp left!");
                    System.out.println(" ");

                    if (hpenemyold > hpenemy) {
                        int damageenemytook = hpenemyold - hpenemy;
                        System.out.println(BLUE + "The "+PURPLE+"WRAITH"+BLUE+" took " + damageenemytook + " damage"+RESET);
                        if (hpenemy <= hpenemymax/2 && !displayed){
                            System.out.println("The "+PURPLE+"WRAITH"+RESET+" is noticeably injured!");
                            displayed = true;
                        } //1/2 way message
                    } else {
                        System.out.println(RED+"The "+YELLOW+"WRAITH "+RED+"blocked your attack!"+RESET);
                    } //enemy dodge msgs
                    System.out.println(" ");

                } // boss fight 2

                if (output == 2 && terrint == 3) {//slime king (stacking damage)
                    hpenemy = 0;
                    accenemy = 0;
                    dmgenemy = 0;

                } // boss fight 3


            }
            //victory conditions
            if (hp > 0){
                System.out.println(GREEN+"==========================");
                System.out.println("========================== ");
                System.out.println("==YOU ARE THE TROLL KING== ");
                System.out.println("========================== ");
                System.out.println("========================== "+RESET);
                System.out.println(" ");
                System.out.println(CYAN+"You exit the dungeon with countless loot and treasure!");
            } else {
                System.out.println(RED+"============================== ");
                System.out.println("============================== ");
                System.out.println("====YOU PERISHED HORRIBLY===== ");
                System.out.println("============================== ");
                System.out.println("============================== "+RESET);
                System.out.println(" ");
                System.out.println(" ");

            }
        } // boss
    }

    public static int grantloot(int difficulty) {
        int loot = 0;
        Random random = new Random();
        loot = 4*difficulty + 6 + random.nextInt(10);  //loot formula
        return loot;
    }

    public static int getenemydesc(int enemydiff, String filename, int lastdiff) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            FileReader readfile = new FileReader(filename);
            BufferedReader readbuffer = new BufferedReader(readfile);

            int length = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                length++;
            }
            Random random = new Random();

                enemydiff = random.nextInt(length) + 1;
                int intdiff = enemydiff - lastdiff;
                int absdiff = Math.abs(intdiff);

                while (absdiff < 2) {
                    enemydiff = random.nextInt(length) + 1;
                    intdiff = lastdiff - enemydiff;
                    absdiff = Math.abs(intdiff);
                }


            int lineNumber = 0;
            //System.out.println(" ");
            for (lineNumber = 1; lineNumber < length + 1; lineNumber++) {
                if (lineNumber == enemydiff) {
                    String text = readbuffer.readLine();
                    System.out.println(RED + "You see an enemy. " + text + lineNumber + '\n' + RESET);


                } else {
                    String text = readbuffer.readLine();
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return enemydiff;
    }

    public static int getroomdesc(int randomize, String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            FileReader readfile = new FileReader("Rooms.txt");
            BufferedReader readbuffer = new BufferedReader(readfile);

            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                i++;
            }
            Random random = new Random();
            randomize = random.nextInt(i) + 1;
            int lineNumber = 0;
            //System.out.println(" ");
            for (lineNumber = 1; lineNumber < i + 1; lineNumber++) {
                if (lineNumber == randomize) {
                    String text = readbuffer.readLine();
                    System.out.println(GREEN + "The room is " + text + '\n' + RESET);

                } else {
                    String text = readbuffer.readLine();
                }
            }

            myReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return randomize;
    }

    public static String inputcommand() {
        System.out.println(BLACK+"What do you want to do?"+RESET);
        Scanner sc = new Scanner(System.in);
        String task = "none";
        task = sc.nextLine();
        task = task.toLowerCase();
        if (!task.equals("")) {
            task = task.substring(0, 1);
        }
        //task = task.substring(0, 1);
        //System.out.println("made it here end of input command");
        return task;
    }

    public static int processcommand(String task, int output, int hpenemy, int accenemy, int dmgenemy, int hp, int acctot, int dmg, int loot, int discount, int crit, boolean[] granted, int dmgred, boolean boss) {

        boolean combat = false;
        //System.out.println("made it here processcommand start");
        if (task.equals("b")) { //bribe
            output = 3;
            if (boss){
                output = 0;
            }
        } //bribe       used: b, t, l, a, s, h
        if (task.equals("u")) { //upgrade
            output = 3;
        } //upgrade
        if (task.equals("l")) { //leave
            output = 1;
            if (boss){
                output = 0;
                System.out.println("You cannot run from me!");
            }
        } //leave
        if (task.equals("a")) { //attack
            output = 2;
            //System.out.println("made it here processcommand process attack");
        } //attack
        if (task.equals("f")) { //stats
            System.out.println("Enemy HP: " + hpenemy);
            System.out.println("Enemy accuracy: " + accenemy);
            System.out.println("Enemy damage: " + dmgenemy);
            System.out.println(" ");
            System.out.println("Your HP: " + hp);
            System.out.println("Your accuracy: " + acctot);
            System.out.println("Your damage: " + dmg);
            System.out.println("Your loot: " + loot);
            System.out.println("Your crit: " + crit);
            System.out.println("Your discount: " + discount);
            System.out.println("Your dmgred: " + dmgred);
        }
        if (task.equals("s")) { //stats
            System.out.println(PURPLE + "Your HP:   " + hp +"  Your HP is your healthiness, if it reaches 0 you die.");
            System.out.println(CYAN + "Your acc:  " + acctot+"   Your accuracy is your %chance to hit an enemy. Your environment affects this stat.");
            System.out.println(RED + "Your dmg:  " + dmg+"   This is how much damage you deal.");
            System.out.println(YELLOW + "Your loot: " + loot+"    This is your accumulated treasure. Spend it wisely.");
            System.out.println(BLUE + "Your crit: " + crit +"    This is a %chance to deal massive damage, and bypass the accuracy check."+ RESET);
            System.out.println("");
        } //stats
        if (task.equals("h")) { //help
            System.out.println("Commands: Attack, Leave, Stats, Help, Upgrade, FullStats, MagicItems. (not case sensitive)");
        } //help
        if (task.equals("m")){ //magicitems
            System.out.println("You have " + granted.length + " magic items.");
            System.out.println("You own:");
            if (granted[0]){
                System.out.print(CYAN+" Spell Scroll;"+RESET);
            }
            if (granted[1]){
                System.out.print(CYAN+" Mythril Armor;"+RESET);
            }
            if (granted[2]){
                System.out.print(CYAN+" Marauder's Helm;" + RESET);
            }
            System.out.println(" ");
        }
        //System.out.println("made it here processcommand return output");
        return output;


    }

    public static int enemyattack(int hpenemy, int accenemy, int dmgenemy, int hp, int acctot, int dmg, int dmgred, String weapon) {
        Random random = new Random();
        boolean hit = true;
        int hitroll = random.nextInt(100);
        int damagedealt = 0;
        if (weapon.equals("b")) {
            dmgred = dmgred + 5;
        }
        if (hitroll > accenemy) {
            hit = false;
        }
        if (hit) {
            damagedealt = dmgenemy + random.nextInt(5) - dmgred;
            if (dmgred > 0) {
                System.out.println(CYAN + dmgred + " damage was negated." + RESET);
            }
        } else {
            damagedealt = 0;
        }
        if (damagedealt < 0){
            damagedealt = 0;
            //System.out.println(YELLOW+"Damage dealt was negative. adjusted to 0"+RESET);
        }
        hp = hp - damagedealt;

        return hp;
    }

    public static int playerattack(int hpenemy, int accenemy, int dmgenemy, int hp, int acctot, int dmg, int crit, String weapon, int critmod) {

        Random random = new Random();
        boolean hit = true;
        int hitroll = random.nextInt(101);
        int damagedealt = 0;

        double dmgd = dmg;

        if (weapon.equals("d")){
            crit = crit + 50;
            dmgd = dmgd*.6;
            System.out.println("You attack sneakily with your " +CYAN+ "Dagger"+RESET+"!");
        }
        if (weapon.equals("s")){
            dmgd = dmgd*1.1;
            System.out.println("You charge forward with your " +CYAN+ "Sword"+RESET+"!");
        }
        if (weapon.equals("b")){
            dmgd = dmgd*0.6;
            acctot = acctot + 10;

            System.out.println("You attack safely at range with your " +CYAN+ "Bow"+RESET+"!");
        }
        if (weapon.equals("a")){
            acctot = acctot - 35;
            dmgd = dmgd*1.2;
            crit = crit + 10;
            System.out.println("You charge forward recklessly with your " +CYAN+ "Axe"+RESET+"!");
        }
        System.out.println(" ");
        dmg = (int) dmgd;

        if (hitroll > acctot) {
            hit = false;
        }
        boolean ifcrit = false;
        int critroll = random.nextInt(101);
        if (critroll < crit) {
            ifcrit = true;
            hit = true;
        }

        double damagedealtd = (double) damagedealt;

        if (ifcrit && hit && critmod == 1) {
            System.out.println(damagedealtd);
            damagedealtd = 2.25 * (dmg + random.nextInt(11));
            System.out.println(damagedealtd);
            System.out.println("You landed a super " + BLUE + "C" + RED + "R" + GREEN + "I" + CYAN + "T" + PURPLE + "I" + YELLOW + "C" + BLUE + "A" + WHITE + "L " + GREEN + "H" + CYAN + "I" + RED + "T" + PURPLE + "!" + RESET);
            System.out.println(" ");
        }
        if (ifcrit && hit && critmod == 0) {
            damagedealtd = 2 * (dmg + random.nextInt(11));
            System.out.println("You landed a " + BLUE + "C" + RED + "R" + GREEN + "I" + CYAN + "T" + PURPLE + "I" + YELLOW + "C" + BLUE + "A" + WHITE + "L " + GREEN + "H" + CYAN + "I" + RED + "T" + PURPLE + "!" + RESET);
            System.out.println(" ");
        }
        if (hit && !ifcrit) {
            damagedealtd = dmg + random.nextInt(11);
        }
        if (!hit) {
            damagedealtd = 0;
        }

        damagedealt = (int) damagedealtd;


        hpenemy = hpenemy - damagedealt;
        return hpenemy;
    }

    public static int[] chooseclass(String choice, int hp, int acc, int dmg, int discount, int crit, int loot, int dmgred) {
        System.out.println("What kind of TROLL are you?");
        System.out.println(YELLOW+"   Merchant "+RESET+"(Discount on prices),");
        System.out.println(RED+"   Barbarian "+RESET+"(Damage reduction),");
        System.out.println(GREEN+"   Fighter "+RESET+"(Stronger starting stats), or ");
        System.out.println(CYAN+"   Ranger "+RESET+"(Higher accuracy)?");
        System.out.println(YELLOW+"   Pacifist "+RESET+"(???)");
        Scanner sc = new Scanner(System.in);
        String choiceabv = "";
        while (!choiceabv.equals("g") && !choiceabv.equals("m") && !choiceabv.equals("b") && !choiceabv.equals("f") && !choiceabv.equals("r") && !choiceabv.equals("p")) {
            choice = sc.nextLine();
            choice = choice.toLowerCase();
            if (!choice.equals("")) {
                choiceabv = choice.substring(0, 1);
            }
        }
        String merc = "m";
        if (choiceabv.equals(merc)) {
            discount = 4;
        }
        String god = "g";
        if (choiceabv.equals(god)) {
            hp = hp + 40000;
            acc = acc + 40000;
            dmg = dmg + 40000;
            discount = discount + 10;
            crit = 100;
            loot = loot + 10000;
        }
        String barb = "b";
        if (choiceabv.equals(barb)) {
            dmgred = dmgred + 5;
        }
        String figh = "f";
        if (choiceabv.equals(figh)) {
            dmg = dmg + 15;
            hp = hp + 20;
        }
        String arch = "r";
        if (choiceabv.equals(arch)) {
            acc = acc + 25;
        }
        String paci = "p";
        if (choiceabv.equals(paci)) {
            dmg = -10;
            dmgred = 100;
        }
        return new int[]{hp, acc, dmg, discount, crit, loot, dmgred};
    }

    public static int[] openshop(int loot, int hp, int acc, int dmg, int discount, int crit, boolean[] granted, int boughtmagic) {
        System.out.println("Spend your earned treasure on upgrades!");
        int cost = 25 - discount;
        int discount2 = 110 - discount*4;
        System.out.println("For sale ("+YELLOW + cost + " loot each"+RESET+"): Potion, Weapon, Skill, Luck. Or Nothing");
        if (boughtmagic < 3) {
            System.out.println("Limited time availability ("+YELLOW+discount2+" loot"+RESET+"): "+CYAN+"artifact"+RESET);
        }
        System.out.println("You have " + YELLOW + loot + RESET + " loot.");
        Scanner sc = new Scanner(System.in);
        Random random = new Random();
        String purch = " ";
        while (loot >= cost) {
            purch = " ";
            while (!purch.equals("p") && !purch.equals("w") && !purch.equals("s") && !purch.equals("n") && !purch.equals("l") && !purch.equals("a")) {
                System.out.println("What would you like to purchase?");
                purch = sc.nextLine();
                purch = purch.toLowerCase();
                if (!purch.equals("")) {
                    purch = purch.substring(0, 1);
                }
            }
            if (purch.equals("p")) {
                int increase = 30 + random.nextInt(20);
                System.out.println("Your HP increased by " + increase);
                hp = hp + increase;
                if (hp > 250){
                    hp = 250;
                    System.out.println(RED+"HP capped!"+RESET);
                }
                System.out.println("Your HP is now " + hp);
                loot = loot - cost;
                System.out.println("You have " + YELLOW + loot + RESET + " loot.");
            }
            if (purch.equals("w")) {
                int increase = 10 + random.nextInt(4);
                System.out.println("Your damage increased by " + increase);
                dmg = dmg + increase;
                System.out.println("Your weapon damage is now " + dmg);
                loot = loot - cost;
                System.out.println("You have " + YELLOW + loot + RESET + " loot.");
            }
            if (purch.equals("s")) {
                int increase = 8 + random.nextInt(4);
                System.out.println("Your accuracy increased by " + increase);
                acc = acc + increase;
                if (acc>200){
                    acc = 200;
                    System.out.println(RED+"Accuracy capped!"+RESET);
                }
                System.out.println("Your accuracy is now " + acc);
                loot = loot - cost;
                System.out.println("You have " + YELLOW + loot + RESET + " loot.");
            }
            if (purch.equals("l")) {
                int increase = 8 + random.nextInt(3);
                System.out.println("Your luck increased by " + increase);
                crit = crit + increase;
                if (crit>100){
                    crit = 101;
                    System.out.println(RED+"Crit% capped!"+RESET);
                }
                System.out.println("Your crit is now " + crit);
                loot = loot - cost;
                System.out.println("You have " + YELLOW + loot + RESET + " loot.");
            }
            if (purch.equals("a")) {
                if (boughtmagic >= 3){
                    System.out.println(RED+"NO MORE MAGIC ITEMS"+RESET);
                } else if (loot < (120 - discount2)) {
                    System.out.println(RED + "YOU CANNOT AFFORD THIS" + RESET);
                } else {
                    loot = loot - 110 + discount;
                    granted = grantmagicitem(granted);
                    boughtmagic++;
                    System.out.println("You have " + YELLOW + loot + RESET + " loot.");
                }
            }
            if (purch.equals("n")) {
                int[] details = {loot, hp, acc, dmg, crit, boughtmagic};
                return details;
            }
        }
        int[] details = {loot, hp, acc, dmg, crit, boughtmagic};
        return details;
    }

    public static boolean[] grantmagicitem(boolean[] granted) {
        Random random = new Random();
        int rand = 1;
        rand = random.nextInt(3);
        while (granted[rand]) {
            rand = random.nextInt(3);

        }
        if (rand == 0) {
            granted[0] = true;
            System.out.println("You have been granted the legendary item: " + CYAN + "Spell Scroll" + RESET);
            System.out.println(CYAN+ "As you recite the mystical incantations, you feel yourself shoved through space"  +RESET);
        } else if (rand == 1) {
            granted[1] = true;
            System.out.println("You have been granted the legendary item: " + CYAN + "Mythril Armor" + RESET);
            System.out.println(WHITE + "This shiny, white armor reduces every instance of damage taken by 10"+ RESET);
        } else {   //rand = 3 or rand == 0??
            granted[2] = true;
            System.out.println("You have been granted the legendary item: " + CYAN + "Marauder's Helm" + RESET);
            System.out.println(WHITE + "This horned headwear grants the user incredible killing resolve, increasing critical damage."+ RESET);
        }
        return granted;
    }

    public static int[] resolvemagicitem(boolean[] granted, int dmgred, int roomnum, int critmod) {
        if (granted[1]){
            dmgred = dmgred + 10;
        }
        if (granted[2]){
            critmod = 1;
        }
        if (granted[0]){
            roomnum = roomnum - 2;
        }
        System.out.println("Resolved magic items");
        return new int[]{dmgred, roomnum, critmod};
    }
}

