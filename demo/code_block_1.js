// playwright.config.js
module.exports = {
    use: {
        baseURL: 'https://example.com', // Replace with actual baseURL
        headless: true,
        screenshot: 'on',
    },
    projects: [
        {
            name: 'chromium',
            use: { browserName: 'chromium' },
        },
        {
            name: 'firefox',
            use: { browserName: 'firefox' },
        },
        {
            name: 'webkit',
            use: { browserName: 'webkit' },
        },
    ],
    timeout: 30000,
    retries: 2,
};