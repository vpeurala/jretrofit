#!/usr/bin/env ruby

primitives = ["boolean", "byte", "char", "short", "int", "long", "float", "double"]

primitive_wrappers = ["Boolean", "Byte", "Character", "Short", "Integer", "Long", "Float", "Double"]

puts <<EOF
package org.jreflect.examples;

public class ExampleClass {
    public static class Structure {
        public String stringField;
        public int intField;
    }
EOF

primitives.each do |java_type|
  puts <<EOF
    public #{java_type} argumentOfMethodWithPrimitive#{java_type.capitalize}Parameter;
EOF
end

primitive_wrappers.each do |java_type|
  puts <<EOF
    public #{java_type} argumentOfMethodWithObject#{java_type}Parameter;
EOF
end

primitives.each do |java_type|
  puts <<EOF

    public void methodWithPrimitive#{java_type.capitalize}Parameter(#{java_type} input) {
        argumentOfMethodWithPrimitive#{java_type.capitalize}Parameter = input;
    }
EOF
end

primitive_wrappers.each do |java_type|
  puts <<EOF

    public void methodWithObject#{java_type}Parameter(#{java_type} input) {
        argumentOfMethodWithObject#{java_type}Parameter = input;
    }
EOF
end

primitives.each do |java_type|
  puts <<EOF

    public #{java_type} methodWithPrimitive#{java_type.capitalize}ReturnValue() {
        return argumentOfMethodWithPrimitive#{java_type.capitalize}Parameter;
    }
EOF
end

primitive_wrappers.each do |java_type|
  puts <<EOF

    public #{java_type} methodWithObject#{java_type}ReturnValue() {
        return argumentOfMethodWithObject#{java_type}Parameter;
    }
EOF
end

puts <<EOF
}
EOF
