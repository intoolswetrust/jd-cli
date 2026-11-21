package com.github.kwart.jd.sample;
public class DensePacked {
    public void a() { System.out.println("a"); } class In1 { In1() { System.out.println("in1"); } void x() { System.out.println("x"); } }
    class In2 { void y() { System.out.println("y"); } } DensePacked() { System.out.println("ctor"); }
    public void b() { System.out.println("b"); } private int f = 1;
    class In3 { int z = 3; In3() { System.out.println("in3"); } void w() { System.out.println("w"); } }
}
