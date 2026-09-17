const fs = require('fs');
let content = fs.readFileSync('./NexusFrontendWeb/src/views/Home.tsx', 'utf8');

content = content.replace(
  "  if (error) {\n    return (\n      <Box sx={{ mt: 4, textAlign: 'center', color: 'error.main' }}>\n        <Typography variant=\"h6\">Failed to load content</Typography>\n        <Typography variant=\"body2\">{error}</Typography>\n      </Box>\n    );\n  }",
  "  if (error) {\n    return (\n      <Box sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center', mt: 10, gap: 2 }}>\n        <Typography color=\"error\">Failed to load content: {error}</Typography>\n        <Button variant=\"contained\" onClick={() => dispatch(loadHomeData())}>Retry</Button>\n      </Box>\n    );\n  }"
);

fs.writeFileSync('./NexusFrontendWeb/src/views/Home.tsx', content);
