import auto_fix_java

for filepath, line_fixes in auto_fix_java.fixes.items():
    if "UmsAdmin" in filepath:
        print(filepath, line_fixes)
