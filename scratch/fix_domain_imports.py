import os
import re

os.chdir("NexusCore")
wildcards = """
import com.nexusengine.core.domain.catalog.entity.*;
import com.nexusengine.core.domain.ordering.entity.*;
import com.nexusengine.core.domain.customer.entity.*;
import com.nexusengine.core.domain.promotion.entity.*;
import com.nexusengine.core.domain.iam.entity.*;
import com.nexusengine.core.domain.catalog.repository.*;
import com.nexusengine.core.domain.ordering.repository.*;
import com.nexusengine.core.domain.customer.repository.*;
import com.nexusengine.core.domain.promotion.repository.*;
import com.nexusengine.core.domain.iam.repository.*;
"""

def process_module(module_path):
    for root, dirs, files in os.walk(module_path):
        for file in files:
            if not file.endswith(".java"): continue
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig = content
            
            # Remove bad specific imports
            content = re.sub(r'import com\.nexusengine\.core\.model\.[a-zA-Z0-9]+;\n?', '', content)
            content = re.sub(r'import com\.nexusengine\.core\.repository\.[a-zA-Z0-9]+;\n?', '', content)
            content = re.sub(r'import com\.nexusengine\.core\.mapper\.[a-zA-Z0-9]+;\n?', '', content)
            
            if "import com.nexusengine.core.model.*;" in content or "import com.nexusengine.core.repository.*;" in content:
                content = content.replace("import com.nexusengine.core.model.*;", wildcards)
                content = content.replace("import com.nexusengine.core.repository.*;", wildcards)
            else:
                imports_end = content.find("import ")
                if imports_end != -1:
                    end_of_line = content.find("\n", imports_end)
                    content = content[:end_of_line+1] + wildcards + content[end_of_line+1:]

            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)

process_module("nexus-admin-api/src/main/java")
process_module("nexus-portal-api/src/main/java")
