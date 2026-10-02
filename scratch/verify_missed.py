import os
import re

exclude_dirs = {'.git', 'node_modules', 'dist', 'target', 'build', 'resource_setup', 'scratch', '.idea', '.vscode', '.vite', 'public'}

issues = {}
def add_issue(filepath, issue):
    if filepath not in issues:
        issues[filepath] = []
    issues[filepath].append(issue)

for root, dirs, files in os.walk('.'):
    dirs[:] = [d for d in dirs if d not in exclude_dirs]
    
    for file in files:
        filepath = os.path.join(root, file)
        
        # Check package.json for old versions
        if file == 'package.json':
            add_issue(filepath, 'Check and update all libraries to their latest stable versions')
        
        # Check for empty files or other obvious issues
        if file.endswith('.java'):
            with open(filepath, 'r', encoding='utf-8') as f:
                content = f.read()
                # Check for other anti-patterns not in scan.py
                if '@RequestMapping' in content and 'method' not in content and '@GetMapping' not in content and '@PostMapping' not in content and '@PutMapping' not in content and '@DeleteMapping' not in content:
                    pass # Just an example, let's just check if there's anything else

for filepath, file_issues in issues.items():
    print(f"{filepath}: {file_issues}")
