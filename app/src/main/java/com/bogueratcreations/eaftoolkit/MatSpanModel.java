package com.bogueratcreations.eaftoolkit;

import android.text.TextUtils;
import android.util.Log;

import java.text.DecimalFormat;

/**
 * Created by jodyroth on 6/30/16.
 * Holds a Matting Span and provides multiple calculations against the base parameters
 */
public class MatSpanModel {
    // Constructor parameters (get/set accessors)
    private String name;
    private int wid;
    private int len;
    private int qty;
    private int lay;  // 0-Brickwork, 1-2-1, 2-3-4 max 6, 3-3-4 max 12;
    private boolean start;  // 1-True if starts with 12' sheet
    private boolean selected;
    // Class defined variables (get accessor)
    private int area = 0;
    private int sheet6 = 0;
    private int sheet12 = 0;
    private int f71 = 0;
    private int f72 = 0;
    private int flatrack = 0;
    private int f44 = 0;
    // Class defined variables (no accessor)
    private int flatrackExtra71 = 0;
    private int flatrackExtra72 = 0;
    private int f44extra6 = 0;
    private int f44extra12 = 0;

    public MatSpanModel(String name, int wid, int len, int qty, int lay, int start) {
        setName(name);
        setWid(wid);
        setLen(len);
        setQty(qty);
        setLay(lay);
        // is converted to bool in setStart function
        setStart(start);
    }

    public MatSpanModel() {
        // provide constructor with default values;
        this("No time for a name.", 96, 96, 1, 0, 0);
    }

    // Getters and Setters for main components of the matting span
    public String getName() {return name;}
    public void setName(String name) {
        // Perform some validation.
        if (TextUtils.isEmpty(name)) {
            this.name = "No time for a name.";
        } else {
            this.name = name;
        }
    }
    public int getWid() {return wid;}
    public void setWid(int wid) {this.wid = wid;}
    public int getLen() {return len;}
    public void setLen(int len) {this.len = len;}
    public int getQty() {return qty;}
    public void setQty(int qty) {this.qty = qty;}
    public int getLay() {return lay;}
    public void setLay(int layPat) {this.lay = layPat;}
    public boolean getStart() {return start;}
    public void setStart(int start) {this.start = (start == 0);}
    public boolean isSelected() {return selected;}
    public void setSelected(int selected) {this.selected = (selected == 1);}
    // Getters for after calculation was made
    public int getArea() {return area;}
    public int getSheet6() {return sheet6;}
    public void setSheet6(int sheet6) {this.sheet6 = sheet6;}
    public int getSheet12() {return sheet12;}
    public void setSheet12(int sheet12){this.sheet12 = sheet12;}
    public int getF71() {return f71;}
    public void setF71(int f71){this.f71 = f71;}
    public int getF72() {return f72;}
    public void setF72(int f72){this.f72 = f72;}
    public int getFlatrack() {return flatrack;}
    public int getF44() {return f44;}
    public void setF44(int f44) {this.f44 = f44;}

    // MARK: - Matting Calculations
    public void calcMat() {
        // Called to update model details so they are available to the view.
        area = len * wid * qty;
        sheet12 = getMat12() * qty;
        sheet6 = getMat6() * qty;
        Log.d("EAF Toolkit", "12s - "+sheet12 + "  6s - "+sheet6);
        f71 = getPallet(sheet12, 18);
        f72 = getPallet(sheet6, 18);
        // Calculate ISO Flatrack Qty based on the most type of sheet.
        if (sheet12 > sheet6) {
            flatrack = getPallet(sheet12, 162);
        } else {
            flatrack = getPallet(sheet6, 162);
        }
        // Calculate extra MatPacs in flatrack due to config
        flatrackExtra71 = (flatrack * 9) - f71;
        flatrackExtra72 = (flatrack * 9) - f72;
        // Calculate F44s and extra sheets
        if (sheet12/16 > sheet6/4) {
            f44 = getPallet(sheet12, 16);
        } else {
            f44 = getPallet(sheet6, 4);
        }
        f44extra12 = (f44 * 16) - sheet12;
        f44extra6 = (f44 * 4) - sheet6;
    }

    public String summarize() {
        // Called to return a summary snippet for email.
        String summary;
        DecimalFormat df = new DecimalFormat("#,###");

        summary = "Span: " + name + "\n";
        if (lay == 0) {summary += "Brickwork Lay";}
        else if (lay == 1) {summary += "2-1 Lay";}
        else if (lay == 2) {summary += "3-4 Max 6' Lay";}
        else if (lay == 3) {summary += "3-4 Max 12' Lay";}
        if (start) {summary += ", starting with 12' sheet.\n";} else {summary+=", starting with 6' sheet.\n";}
        summary += "W x L x # : " + df.format(wid) + " X " + df.format(len) + " X " + qty + " = " + df.format(area) + " SqFt.\n";
        summary += "Sheets 12'/6' : " + df.format(sheet12) + " / " + df.format(sheet6) + "\n";
        summary += "F-71 / F-72 : " + df.format(f71) + " / " + df.format(f72) + "\n";
        summary += "ISO Flatracks : " + df.format(flatrack) + " with " + df.format(flatrackExtra71) + " extra F-71s and " + df.format(flatrackExtra72) + " extra F-72s in this span config.\n";
        summary += "Or only F-44s : " + df.format(f44) + " with " + df.format(f44extra12) + " extra 12' sheets and " + df.format(f44extra6) + " extra 6' sheets\n";

        return summary;
    }

    public String summarizeMultiple() {

        // need to set Sheets, and F-pkgs in model then run this to generate a report for multiple spans
        // Calculate ISO Flatrack Qty based on the most type of sheet.

        if (f71 > f72) {
            // gets number of pallets required to ship to different destinations
            flatrack = getPallet(f71, 9);
        } else {
            flatrack = getPallet(f72, 9);
        }
        // Calculate extra MatPacs in flatrack due to config
        flatrackExtra71 = (flatrack * 9) - f71;
        flatrackExtra72 = (flatrack * 9) - f72;
        // Calculate extra sheets in F44
        f44extra12 = (f44 * 16) - sheet12;
        f44extra6 = (f44 * 4) - sheet6;

        String summary;
        DecimalFormat df = new DecimalFormat("#,###");

        summary =  "MULTIPLE SPANS, single shipment.\n\n";
        summary += "Sheets 12'/6' : " + df.format(sheet12) + " / " + df.format(sheet6) + "\n";
        summary += "F-71 / F-72 : " + df.format(f71) + " / " + df.format(f72) + "\n";
        summary += "ISO Flatracks : " + df.format(flatrack) + " with " + df.format(flatrackExtra71) + " extra F-71s and " + df.format(flatrackExtra72) + " extra F-72s in this shipping config.\n";
        summary += "Or if only using F-44s : " + df.format(f44) + " with " + df.format(f44extra12) + " extra 12' sheets and " + df.format(f44extra6) + " extra 6' sheets\n";
        summary += "\n***** INDIVIDUAL SPAN DETAILS *****\n\n";
        return summary;
    }

    //  **** Supporting Methods
    private static Integer getBW6(int len, int wid, boolean startLay) {
        int sheets;
        int start;
        if (startLay){start=12;}else{start=6;}
        if (len == 0) {
            sheets = 0;
        } else if (len == 2) {
            if ((start == 12) && (wid % 12 == 0)) {
                sheets = 0;
            } else if ((start == 6) && (wid % 12 == 0)) {
                sheets = 2;
            } else {
                sheets = 1;
            }
        } else {
            if (len % 4 == 0) {
                sheets = len / 2;
            } else {
                sheets = (len - 2) / 2;
                if ((start == 6) && (wid % 12 == 0)) {
                    sheets = sheets + 2;
                    // Updated following to else if for width ! divisible by 12 otherwise no adjustment needed
                } else if (wid % 12 == 6){
                    sheets = sheets + 1;
                }
            }
        }
        return sheets;
    }

    private Integer getMat6() {
        int sheets = 0;
        int extraRows;

        if (lay == 1) { // 2-1 Lay
            extraRows = len % 6;
            switch (extraRows) {
                case 0:
                    return (len * wid / 36);
                case 2:
                    return getBW6(2, wid, start) + ((len - 2) * wid / 36);
                case 4:
                    return getBW6(4, wid, start) + ((len - 4) * wid / 36);
            }
        } // end 2-1 lay evaluation
        else if (lay == 0) { // Brickwork
            return getBW6(len, wid, start);
        }
        else if (lay == 2) { // 3-4 Lay max 6
            extraRows = len % 14;
            sheets = (getBW6(6, wid, start) + (wid / 6) * 4) * (len / 14);
            switch (extraRows) {
                case 2:
                case 4:
                case 6:
                    return sheets + getBW6(extraRows, wid, start);
                case 8:
                case 10:
                case 12:
                    return sheets + getBW6(6, wid, start) + ((wid / 6) * ((extraRows / 2) - 3));
            }
            return sheets;
        } else if (lay == 3) {  //3-4 Lay max 12
            if (len > 13) {
                sheets = getBW6(6, wid, start);
                // Adds additional sheets for the 4 rows that must have a 6' due to width
                if (wid % 12 == 6) {
                    sheets = sheets + 4;
                }
                // multiply full legs by # of sheets in each 3-4 lay section
                sheets = sheets * (len / 14);
            }
            extraRows = len % 14;
            switch (extraRows) {
                case 2:
                case 4:
                case 6:
                    sheets = sheets + getBW6(extraRows, wid, start);
                    break;
                case 8:
                case 10:
                case 12:
                    sheets = sheets + getBW6(6, wid, start);
                    if (wid % 12 == 6) {
                        sheets = sheets + ((extraRows / 2) - 3);
                    }
                    break;
            }
            return sheets;
        } // end 3-4 Max 12 evaluation
        // Start 3-4 Custom evaluation
//        if (lay == 4) {
//            extraRows = len % 14;
//            sheets = getBW6(6, wid, start) * (len / 14);
//            sheets = sheets + ((len / 14) * (cust46 + cust56 + cust66 + cust76));
//            switch (extraRows) {
//                case 2:
//                case 4:
//                case 6:
//                    return sheets + getBW6(extraRows, wid, start);
//                case 8:
//                    return sheets + getBW6(6, wid, start) + cust46;
//                case 10:
//                    return sheets + getBW6(6, wid, start) + cust46 + cust56;
//                case 12:
//                    return sheets + getBW6(6, wid, start) + cust46 + cust56 + cust66;
//            }
//        }
        return sheets;
    } // end getMat6

    private static Integer getBW12(int len, int wid, boolean startLay) {
        Integer start;

        if (startLay){start=12;}else{start=6;}
        if (len == 0) {
            return 0;
        }
        if (len == 2) {
            if ((start == 12) && (wid % 12 == 0)) {
                return wid / 12;
            } else {
                return ((wid - 6) / 12);
            }
        } else {
            if (len % 4 == 0) {
                return ((wid / 6) - 1) * (len / 4);
            } else {
                if (start == 12) {
                    return ((wid / 6) - 1) * (len - 2) / 4 + (wid / 12);
                } else {
                    return ((wid / 6) - 1) * (len - 2) / 4 + ((wid - 6) / 12);
                }
            }
        }
    } // end getBW12

    private Integer getMat12() {
        int sheets = 0;
        int extraRows;
        if (lay == 1) {
            extraRows = len % 6;
            if (extraRows == 0) {
                return len * wid / 36;
            }
            if (extraRows == 2) {
                return getBW12(2, wid, start) + ((len - 2) * wid / 36);
            }
            if (extraRows == 4) {
                return getBW12(4, wid, start) + ((len - 4) * wid / 36);
            }
        } // end 2-1 lay evaluation
        if (lay == 0) {
            // need to use this repeatedly so call separate function.
            return getBW12(len, wid, start);
        } // end 3-4 Lay Max 6 evaluation
        if (lay == 2) {
            if (len > 13) {
                sheets = getBW12(6, wid, start) * (len / 14);
            }
            extraRows = len % 14;
            switch (extraRows) {
                case 2:
                case 4:
                case 6:
                    return sheets + getBW12(extraRows, wid, start);
                case 8:
                case 10:
                case 12:
                    return sheets + getBW12(6, wid, start);
            }
            return sheets;
        } // end 3-4 lay Max 6 evaluation
        if (lay == 3) {
            if (len > 13) {
                sheets = getBW12(6, wid, start) * (len / 14);
                sheets = sheets + ((len / 14) * (wid / 12) * 4);
            } // end if
            extraRows = len % 14;
            switch (extraRows) {
                case 2:
                case 4:
                case 6:
                    return sheets + getBW12(extraRows, wid, start);
                case 8:
                case 10:
                case 12:
                    return sheets + getBW12(6, wid, start) + ((wid / 12) * ((extraRows / 2) - 3));
            }
            return sheets;
        } // end 3-4 Lay Max 12 evaluation
//        if (lay == 4) {
//            if (len > 13) {
//                sheets = getBW12(6, wid, start) * (len / 14);
//                sheets = sheets + (len / 14) * (cust412 + cust512 + cust612 + cust712);
//            } // end if
//            extraRows = len % 14;
//            switch (extraRows) {
//                case 2:
//                case 4:
//                case 6:
//                    return sheets + getBW12(extraRows, wid, start);
//                case 8:
//                    return sheets + getBW12(6, wid, start) + cust412;
//                case 10:
//                    return sheets + getBW12(6, wid, start) + cust412 + cust512;
//                case 12:
//                    return sheets + getBW12(6, wid, start) + cust412 + cust512 + cust612;
//            }
//            return sheets;
//        } // end 3-4 Lay Custom evaluation
        return sheets;
    } // end getMat12

    public static int getPallet(int qty, int perPallet) {
        double pallets = Math.ceil((double) qty / perPallet);
        return (int) pallets;
    }


//
//    public static String getPallet(int qty, int perPallet) {
//        double pallets = Math.ceil((double)qty / perPallet);
//        Integer pal = (int)pallets;
//        // Cast one int variable to double else it works as int
//        Log.e("DEBUG:", "getPallet returns " + pal.toString());
//        return pal.toString();
//    }

}
