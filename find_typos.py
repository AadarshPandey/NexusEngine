import re

known_typos = {
    'feight': 'freight',
    'vertify': 'verify',
    'prefrence': 'preference',
    'intergration': 'integration',
    'replay': 'reply',
    'luckey': 'lucky',
    'priviledge': 'privilege',
    'couont': 'count',
    'recomend': 'recommend'
}

with open('schema.txt', 'r') as f:
    lines = f.readlines()

results = set()

for line in lines:
    if '|' not in line: continue
    table, column = [p.strip() for p in line.split('|')]
    
    for typo, correction in known_typos.items():
        if typo in table:
            results.add(f"Table: {table} -> contains '{typo}', should be '{correction}'")
        if typo in column:
            results.add(f"Column: {column} (in table {table}) -> contains '{typo}', should be '{correction}'")

for r in sorted(list(results)):
    print(r)
