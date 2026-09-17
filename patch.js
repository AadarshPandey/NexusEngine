const fs = require('fs');
let content = fs.readFileSync('./NexusFrontendWeb/src/views/Profile.tsx', 'utf8');

content = content.replace('const [loading, setLoading] = useState(true);', 'const [loading, setLoading] = useState(true);\n  const [error, setError] = useState<string | null>(null);');

content = content.replace(
  'const loadDashboardData = async () => {\n    try {\n      setLoading(true);',
  'const loadDashboardData = async () => {\n    try {\n      setLoading(true);\n      setError(null);'
);

content = content.replace(
  "} catch (error) {\n      console.error('Failed to load dashboard data', error);",
  "} catch (error) {\n      console.error('Failed to load dashboard data', error);\n      setError(error instanceof Error ? error.message : 'Failed to load dashboard data');"
);

content = content.replace(
  "if (loading) {\n    return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;\n  }",
  "if (loading) {\n    return <Box sx={{ display: 'flex', justifyContent: 'center', mt: 10 }}><CircularProgress /></Box>;\n  }\n\n  if (error) {\n    return (\n      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mt: 10, gap: 2 }}>\n        <Typography color=\"error\">{error}</Typography>\n        <Button variant=\"contained\" onClick={loadDashboardData}>Retry</Button>\n      </Box>\n    );\n  }"
);

fs.writeFileSync('./NexusFrontendWeb/src/views/Profile.tsx', content);
