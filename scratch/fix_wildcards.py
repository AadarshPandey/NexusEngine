import os
import glob
import re

os.chdir("NexusCore")
def process_module(module_path, prefix):
    for root, dirs, files in os.walk(module_path):
        for file in files:
            if not file.endswith(".java"): continue
            filepath = os.path.join(root, file)
            with open(filepath, "r") as f:
                content = f.read()
                
            orig = content
            
            # Remove bad specific imports
            content = re.sub(r'import com\.nexusengine\.core\.service\.[a-zA-Z0-9]+;\n?', '', content)
            content = re.sub(r'import com\.nexusengine\.core\.dto\.[a-zA-Z0-9]+;\n?', '', content)
            if prefix == 'admin':
                content = re.sub(r'import com\.nexusengine\.core\.portal\..*?;\n?', '', content)
            if prefix == 'portal':
                content = re.sub(r'import com\.nexusengine\.core\.admin\..*?;\n?', '', content)
            
            # Replace wildcards with all the new packages
            wildcards = f"""
import com.nexusengine.core.{prefix}.catalog.dto.*;
import com.nexusengine.core.{prefix}.ordering.dto.*;
import com.nexusengine.core.{prefix}.customer.dto.*;
import com.nexusengine.core.{prefix}.promotion.dto.*;
import com.nexusengine.core.{prefix}.iam.dto.*;
import com.nexusengine.core.{prefix}.catalog.service.*;
import com.nexusengine.core.{prefix}.ordering.service.*;
import com.nexusengine.core.{prefix}.customer.service.*;
import com.nexusengine.core.{prefix}.promotion.service.*;
import com.nexusengine.core.{prefix}.iam.service.*;
"""
            if prefix == "portal":
                wildcards += "import com.nexusengine.core.portal.home.dto.*;\nimport com.nexusengine.core.portal.home.service.*;\n"
            
            if "import com.nexusengine.core.dto.*;" in content or "import com.nexusengine.core.service.*;" in content:
                content = content.replace("import com.nexusengine.core.dto.*;", wildcards)
                content = content.replace("import com.nexusengine.core.service.*;", wildcards)
            else:
                # Add to the end of imports
                imports_end = content.rfind("import ")
                if imports_end != -1:
                    end_of_line = content.find("\n", imports_end)
                    content = content[:end_of_line+1] + wildcards + content[end_of_line+1:]

            # FlagValidator is in com.nexusengine.core.common.validator
            # but wait, where is it actually? Let's assume we can import it.
            # I'll just add it.
            content = content.replace("import com.nexusengine.core.validator.FlagValidator;", "import com.nexusengine.core.common.validator.FlagValidator;")
            if "FlagValidator" in content and "import com.nexusengine.core.common.validator.FlagValidator" not in content:
                content = content.replace("import java.", "import com.nexusengine.core.common.validator.FlagValidator;\nimport java.", 1)

            if content != orig:
                with open(filepath, "w") as f:
                    f.write(content)
            print(f"Processed {filepath}")

process_module("nexus-admin-api/src/main/java", "admin")
process_module("nexus-portal-api/src/main/java", "portal")
