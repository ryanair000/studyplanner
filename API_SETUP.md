# Student Copilot API Setup

## ✅ Netlify Deployment Configuration

### Site Details
- **Site ID**: `7016a1fc-ee1a-4a3f-a123-b22cf46044e0`
- **Site Name**: `luminous-pithivier-ef07aa`
- **Custom Domain**: `studyplanner.qybrrlabs.frica`
- **Default Domain**: `luminous-pithivier-ef07aa.netlify.app`
- **Admin Panel**: https://app.netlify.com/projects/luminous-pithivier-ef07aa

### API Endpoint Configuration
The Android app is configured to use:
```
BASE_URL = https://studyplanner.qybrrlabs.frica/
```

### API Endpoint Details

**Study Plan Generation Endpoint**:
```
POST https://studyplanner.qybrrlabs.frica/api/study-plan
```

**Request Format**:
```json
{
  "assignments": [
    {
      "id": 1,
      "title": "Assignment Title",
      "course_name": "Course Name",
      "due_date": 1704067200000
    }
  ],
  "exams": [
    {
      "id": 1,
      "title": "Exam Title",
      "course_name": "Course Name",
      "exam_date": 1704067200000
    }
  ]
}
```

**Response Format**:
```json
{
  "study_plan": "📚 YOUR PERSONALIZED STUDY PLAN...",
  "status": "success"
}
```

## Functions

### study-plan.ts (Netlify Function)

Located at: `functions/study-plan.ts`

**Features**:
- Accepts POST requests with assignments and exams
- Generates a personalized study plan
- Categorizes tasks by urgency (within 7 days = urgent)
- Provides daily study recommendations
- Returns markdown-formatted study plan text

**Function Endpoint**: `/.netlify/functions/study-plan`

**Redirected to**: `/api/study-plan` (via netlify.toml)

## Deployment Steps

### Option 1: Deploy via Netlify CLI
```bash
cd StudentCopilot
NETLIFY_AUTH_TOKEN="nfp_5usdLEQRR2VPRQxvoVKBdNTAXe831hKe6e0e" \
netlify deploy --prod
```

### Option 2: Deploy via Git
1. Push code to a Git repository (GitHub, GitLab, Bitbucket)
2. Connect the repository to Netlify
3. Set the build command: `npm run build`
4. Set the publish directory: `public` (or leave empty)
5. Netlify will auto-deploy on every push

### Option 3: Manual Deploy
```bash
netlify deploy --prod --dir=public --functions=functions
```

## DNS Configuration (If Not Using Netlify DNS)

If `studyplanner.qybrrlabs.frica` is on a different DNS provider, add:

```
CNAME Record:
Name: studyplanner
Value: luminous-pithivier-ef07aa.netlify.app
TTL: 3600 (or default)
```

## Environment Variables

Currently, the API has no environment variables configured. To add them:

```bash
NETLIFY_AUTH_TOKEN="nfp_5usdLEQRR2VPRQxvoVKBdNTAXe831hKe6e0e" \
netlify env:set VARIABLE_NAME "value"
```

## Local Development

### Run Netlify Functions Locally
```bash
netlify dev
```

This will:
- Start a local Netlify dev server on `localhost:8888`
- Serve functions at `http://localhost:8888/.netlify/functions/*`
- Watch for code changes and reload

### Test the Study Plan Endpoint
```bash
curl -X POST http://localhost:8888/.netlify/functions/study-plan \
  -H "Content-Type: application/json" \
  -d '{
    "assignments": [
      {
        "id": 1,
        "title": "Math Homework",
        "course_name": "Calculus I",
        "due_date": 1704067200000
      }
    ],
    "exams": []
  }'
```

## Files

- **netlify.toml** - Netlify configuration (build command, redirects, functions)
- **functions/study-plan.ts** - Netlify Function for study plan generation
- **package.json** - Node.js dependencies

## Next Steps

1. **Commit to Git** (if using Git deployment)
   ```bash
   git add .
   git commit -m "Add Netlify API setup and study plan function"
   git push origin main
   ```

2. **Test the Endpoint** from your Android app
   - The app will make requests to: `https://studyplanner.qybrrlabs.frica/api/study-plan`

3. **Monitor Deployments** at: https://app.netlify.com/projects/luminous-pithivier-ef07aa/deploys

4. **View Logs** for debugging:
   ```bash
   NETLIFY_AUTH_TOKEN="nfp_5usdLEQRR2VPRQxvoVKBdNTAXe831hKe6e0e" \
   netlify logs --tail
   ```

## Troubleshooting

**CORS Issues**: If the Android app gets CORS errors, check the Netlify function headers. By default, Netlify Functions support CORS.

**DNS Not Resolving**: If `studyplanner.qybrrlabs.frica` doesn't resolve, verify the CNAME record is set correctly at your DNS provider.

**404 on /api/study-plan**: Ensure the redirect in `netlify.toml` is correctly configured to redirect to `/.netlify/functions/study-plan`.

**Build Failures**: Check the build logs at https://app.netlify.com/projects/luminous-pithivier-ef07aa/deploys
