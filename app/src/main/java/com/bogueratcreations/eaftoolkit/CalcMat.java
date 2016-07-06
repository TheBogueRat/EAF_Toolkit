package com.bogueratcreations.eaftoolkit;

import android.util.Log;

/**
 * Created by TheBogueRat on 6/26/2015.
 * Calculates the amount of matting for a given span
 */
class CalcMat {

    /**
     *
     * @param wid The span width
     * @param len The span length
     * @param startLay Boolean representing if starting with 12' sheet
     * @return The number of 6 foot sheets required for brickwork span of specified length.
     */
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
                } else {
                    sheets = sheets + 1;
                }
            }
        }
        return sheets;
    }
    /**
     *
     * @param wid The span width
     * @param len The span length
     * @param start Boolean representing if starting with 12' sheet
     * @param lay  Int representing layPattern requested
     *             @param cust46
     * @return The number of 6 foot sheets required for param.
     */
    public static Integer getMat6(int len,int wid, boolean start, int lay,int cust46,int cust56,int cust66,int cust76) {
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
        if (lay == 0) { // Brickwork
            return getBW6(len, wid, start);
        }
        if (lay == 2) { // 3-4 Lay max 6
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
        }
        if (lay == 3) {  //3-4 Lay max 12
            extraRows = len % 14;
            if (len > 13) {
                sheets = getBW6(6, wid, start);
                if (wid % 12 == 6) {
                    sheets = sheets + 4;
                }
                // get the total matting for full legs of the pattern.
                sheets = sheets * (len / 14);
            }
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
            }
            return sheets;
        } // end 3-4 Max 12 evaluation
        // Start 3-4 Custom evaluation
        if (lay == 4) {
            extraRows = len % 14;
            sheets = getBW6(6, wid, start) * (len / 14);
            sheets = sheets + ((len / 14) * (cust46 + cust56 + cust66 + cust76));
            switch (extraRows) {
                case 2:
                case 4:
                case 6:
                    return sheets + getBW6(extraRows, wid, start);
                case 8:
                    return sheets + getBW6(6, wid, start) + cust46;
                case 10:
                    return sheets + getBW6(6, wid, start) + cust46 + cust56;
                case 12:
                    return sheets + getBW6(6, wid, start) + cust46 + cust56 + cust66;
            }
        }
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

    public static Integer getMat12(int len,int wid,boolean start,int lay,int cust412,int cust512,int cust612,int cust712) {
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
        if (lay == 4) {
            if (len > 13) {
                sheets = getBW12(6, wid, start) * (len / 14);
                sheets = sheets + (len / 14) * (cust412 + cust512 + cust612 + cust712);
            } // end if
            extraRows = len % 14;
            switch (extraRows) {
                case 2:
                case 4:
                case 6:
                    return sheets + getBW12(extraRows, wid, start);
                case 8:
                    return sheets + getBW12(6, wid, start) + cust412;
                case 10:
                    return sheets + getBW12(6, wid, start) + cust412 + cust512;
                case 12:
                    return sheets + getBW12(6, wid, start) + cust412 + cust512 + cust612;
            }
            return sheets;
        } // end 3-4 Lay Custom evaluation
        return sheets;
    } // end getMat12

    public static String getPallet(int qty, int perPallet) {
        double pallets = Math.ceil((double)qty / perPallet);
        Integer pal = (int)pallets;
        // Cast one int variable to double else it works as int
        Log.e("DEBUG:", "getPallet returns " + pal.toString());
        return pal.toString();
    }

}
