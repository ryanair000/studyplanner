import { Handler } from "@netlify/functions";

interface Assignment {
  id: number;
  title: string;
  course_name: string;
  due_date: number;
}

interface Exam {
  id: number;
  title: string;
  course_name: string;
  exam_date: number;
}

interface StudyPlanRequest {
  assignments: Assignment[];
  exams: Exam[];
}

const handler: Handler = async (event) => {
  if (event.httpMethod !== "POST") {
    return {
      statusCode: 405,
      body: JSON.stringify({ error: "Method not allowed" }),
    };
  }

  try {
    const payload: StudyPlanRequest = JSON.parse(event.body || "{}");
    const { assignments = [], exams = [] } = payload;

    // Generate study plan based on tasks
    const studyPlan = generateStudyPlan(assignments, exams);

    return {
      statusCode: 200,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        study_plan: studyPlan,
        status: "success",
      }),
    };
  } catch (error) {
    return {
      statusCode: 400,
      body: JSON.stringify({
        study_plan: "Error processing request",
        status: "error",
      }),
    };
  }
};

function generateStudyPlan(
  assignments: Assignment[],
  exams: Exam[]
): string {
  const now = Date.now();
  const allTasks = [
    ...assignments.map((a) => ({
      title: a.title,
      course: a.course_name,
      deadline: a.due_date,
      type: "assignment",
    })),
    ...exams.map((e) => ({
      title: e.title,
      course: e.course_name,
      deadline: e.exam_date,
      type: "exam",
    })),
  ];

  if (allTasks.length === 0) {
    return "No upcoming tasks to plan for. Great job staying on top of your coursework!";
  }

  // Sort by deadline
  allTasks.sort((a, b) => a.deadline - b.deadline);

  // Calculate daily study load
  const urgentTasks = allTasks.filter(
    (t) => t.deadline - now <= 7 * 24 * 60 * 60 * 1000
  );
  const upcomingTasks = allTasks.filter(
    (t) => t.deadline - now > 7 * 24 * 60 * 60 * 1000
  );

  let plan = "📚 YOUR PERSONALIZED STUDY PLAN\n\n";

  plan += "🔴 URGENT (Due within 7 days):\n";
  if (urgentTasks.length === 0) {
    plan += "  • No urgent tasks\n";
  } else {
    urgentTasks.forEach((task) => {
      const daysLeft = Math.ceil(
        (task.deadline - now) / (24 * 60 * 60 * 1000)
      );
      plan += `  • [${task.type.toUpperCase()}] ${task.title} (${task.course})\n`;
      plan += `    Due in ${daysLeft} day${daysLeft !== 1 ? "s" : ""}\n`;
    });
  }

  plan += "\n📅 UPCOMING (Next 14-30 days):\n";
  if (upcomingTasks.length === 0) {
    plan += "  • No upcoming tasks\n";
  } else {
    upcomingTasks.slice(0, 5).forEach((task) => {
      const daysLeft = Math.ceil(
        (task.deadline - now) / (24 * 60 * 60 * 1000)
      );
      plan += `  • [${task.type.toUpperCase()}] ${task.title} (${task.course})\n`;
      plan += `    Due in ${daysLeft} days\n`;
    });
  }

  plan += "\n💡 STUDY TIPS:\n";
  plan += "  • Tackle urgent tasks first\n";
  plan += "  • Break large assignments into smaller chunks\n";
  plan += "  • Study for exams a little bit every day\n";
  plan += "  • Stay organized and consistent!\n";

  return plan;
}

export { handler };
