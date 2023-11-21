// This file is managed by terraform in https://github.com/Censio/github-config/
// Any changes made to this file will be overwritten
module.exports = {
  extends: ['@commitlint/config-conventional'],
  parserPreset: {
    parserOpts: {
      issuePrefixes: ['[A-Z]{2,}-[0-9]+']
    }
  },
  rules: {
    'type-enum': [2, 'always', ['feat', 'fix', 'chore', 'docs', 'style', 'refactor', 'test', 'ci', 'build', 'revert', 'release']],
    'type-case': [2, 'always', 'lower-case'],
    'type-empty': [2, 'never'],
    'subject-case': [0],
    'subject-empty': [2, 'never'],
    'references-empty': [2, 'never']
  }
}
// Conventional Commits
// Docs: https://www.conventionalcommits.org/en/v1.0.0/
// Format:
// <type>[optional scope]: <subject>
//
// [optional body]
//
// [optional footer(s)]

// Commit lint rules
// Docs: https://github.com/conventional-changelog/commitlint/blob/master/docs/reference-rules.md
// Each rule has the following 3 parameters:
// 0. Level [0..2]: 0 disables the rule. For 1 it will be considered a warning for 2 an error.
// 1. Applicable always|never: never inverts the rule.
// 2. Optional value: value to use for this rule.
