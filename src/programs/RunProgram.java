/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package programs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *
 * @author Lwdthe1
 */
public class RunProgram {

    public static void main(String[] args) throws IOException {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String s;

        while ((s = in.readLine()) != null) {

            int n;
            int p;
            int q;

            String w = s.substring(0, s.indexOf(" "));

            int l = Integer.parseInt(w);

            int k = 0;
            while (!s.substring(k, k + 1).contains(" ") && k < w.length()) {
                k++;
            }

            n = l;
            p = Integer.parseInt(s.substring(k + 1, k + 2));
            q = Integer.parseInt(s.substring(k + 3));

            String o = "";
            for (int j = 1; j <= n; j++) {
                if (j % p == 0 && j % q == 0) {
                    o += "WATSON ";
                } else if (j % p == 0) {
                    o += "WAT ";
                } else if (j % q == 0) {
                    o += "SON ";
                } else {
                    o += j + " ";
                }
            }
            System.out.println(o.trim());

        }
    }
}
