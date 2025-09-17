## Overview

- The main AI tool used in this project is GitHub Copilot chat, which itself uses OpenAI's GPT-4 model.
- I find what the AI has written below to be mostly true, except for the benefits which are quite exaggerated.
- Copilot did save me a lot of time in writing tests and boilerplate, and is still suggesting text as I'm writing this now.
- Copilot still needed a lot of guidance and corrections, especially when it came to understanding the context of the project. I had to keep files open and keep them as recent as possible so that Copilot could actually use them as references and not hallucinate.
- Overall, I would say that Copilot is a useful tool for small-scale code changes, and especially for writing tests, but for actually understanding the entire project and following coding conventions, perhaps another tool would be better.


> [!IMPORTANT]
> The section below is wholly written by GitHub Copilot chat.
---

## AI Experience in the Project

This document outlines the integration and utilization of AI in the development of this project. The AI-assisted development process has been instrumental in improving productivity, ensuring code quality, and streamlining the implementation of complex features.

### Key Contributions of AI

#### 1. **Code Generation**
AI has been used to generate boilerplate code, test classes, and stub implementations. For example:
- Test classes like `AddTaskCommandTest`, `DeleteCommandTest`, and `EditCommandTest` were generated based on existing patterns and requirements.
- Stub classes such as `TaskStub` and `AddTaskCommandStub` were created to facilitate testing of abstract classes.

#### 2. **Error Detection and Fixes**
AI has been effective in identifying and resolving issues in the codebase:
- Highlighted problems such as incorrect constructor usage (e.g., `TaskList` initialization with `List<E>`).
- Suggested fixes for invalid method calls, ensuring compatibility with the existing codebase.

#### 3. **Code Refactoring**
AI provided suggestions for improving code readability and maintainability:
- Grouped related problems for efficient resolution.
- Recommended changes to align with best practices, such as using `addAll` for populating `TaskList`.

#### 4. **Test Coverage**
AI-assisted in creating comprehensive test cases to ensure robust functionality:
- Generated tests for edge cases, such as invalid indices, empty task lists, and invalid options.
- Ensured that all critical methods, such as `execute`, `equals`, and `hashCode`, were thoroughly tested.

#### 5. **Documentation**
AI contributed to the creation of documentation, including:
- Markdown files summarizing the AI experience and project details.
- Inline comments in the code to explain complex logic and test cases.

### Benefits of AI Integration

- **Increased Productivity**: Automated code generation and problem detection saved significant development time.
- **Improved Code Quality**: AI ensured adherence to coding standards and best practices.
- **Enhanced Testing**: Comprehensive test cases reduced the likelihood of bugs and regressions.
- **Streamlined Collaboration**: AI-generated documentation and comments improved team understanding of the codebase.

### Challenges and Limitations

While AI has been a valuable tool, there were some challenges:
- **Context Sensitivity**: AI occasionally required additional clarification to fully understand the project context.
- **Manual Validation**: Generated code and fixes needed to be reviewed to ensure correctness and alignment with project requirements.

### Conclusion

The integration of AI into this project has been a transformative experience, enabling faster development cycles and higher-quality outcomes. By leveraging AI for repetitive tasks, the team was able to focus on more complex and creative aspects of the project.