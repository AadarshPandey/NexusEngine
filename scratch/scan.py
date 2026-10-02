import os
import re

directories_to_scan = [
    'NexusCore',
    'NexusAdminPanel',
    'NexusFrontendWeb',
    '.'
]

exclude_dirs = {'.git', 'node_modules', 'dist', 'target', 'build', 'resource_setup', 'scratch', '.idea', '.vscode', '.vite', 'public'}

java_anti_patterns = {
    r'@Autowired': 'Field injection (use constructor injection)',
    r'System\.out\.println': 'Use logger instead of System.out.println',
    r'System\.err\.println': 'Use logger instead of System.err.println',
    r'\.printStackTrace\(\)': 'Use logger instead of printStackTrace',
    r'import java\.util\.Date;': 'Use java.time instead of java.util.Date',
    r'Thread\.sleep': 'Avoid Thread.sleep (except in tests)',
    r'catch\s*\([^{]+\)\s*\{\s*\}': 'Empty catch block'
}

ts_anti_patterns = {
    r'console\.log': 'Remove console.log',
    r'\bvar\b': 'Use let/const instead of var',
    r':\s*any\b': 'Avoid using "any" type'
}

def scan_file(filepath):
    issues = []
    if filepath.endswith('.java'):
        patterns = java_anti_patterns
    elif filepath.endswith(('.ts', '.tsx', '.js', '.jsx')):
        patterns = ts_anti_patterns
    else:
        return issues

    try:
        with open(filepath, 'r', encoding='utf-8') as f:
            content = f.read()
            for pattern, message in patterns.items():
                if re.search(pattern, content):
                    issues.append(message)
    except Exception:
        pass
    return issues

def main():
    results = {}
    for root, dirs, files in os.walk('.'):
        dirs[:] = [d for d in dirs if d not in exclude_dirs]
        
        for file in files:
            filepath = os.path.join(root, file)
            issues = scan_file(filepath)
            
            if file == 'pom.xml':
                try:
                    with open(filepath, 'r', encoding='utf-8') as f:
                        content = f.read()
                        if '<java.version>' in content and '<java.version>25</java.version>' not in content:
                            issues.append('Update Java version to 25')
                        if 'spring-boot-starter-parent' in content and '<version>4.1.1</version>' not in content:
                            issues.append('Update Spring Boot to 4.1.1')
                except:
                    pass
            
            if file == 'docker-compose.yml':
                 issues.append('Check services and update all image versions to the latest stable')

            if issues:
                rel_path = os.path.relpath(filepath, '.')
                folder = os.path.dirname(rel_path)
                if not folder:
                    folder = '.'
                if folder not in results:
                    results[folder] = []
                results[folder].append((file, issues))

    with open('scratch/cleanup_append.md', 'w') as f:
        for folder in sorted(results.keys()):
            f.write(f"\n### {folder}\n")
            for file, issues in sorted(results[folder]):
                f.write(f"- `{folder}/{file}`: {', '.join(issues)}\n")

if __name__ == '__main__':
    main()
