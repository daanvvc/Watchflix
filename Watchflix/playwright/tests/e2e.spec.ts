import { test, expect } from '@playwright/test';

test('test', async ({ page }) => {
  await page.goto('http://localhost:5173/');
  await page.getByRole('textbox', { name: 'Email address' }).click();
  await page.getByRole('textbox', { name: 'Email address' }).fill('john.doe@example.com');
  await page.getByRole('textbox', { name: 'Email address' }).press('Tab');
  await page.getByRole('textbox', { name: 'Your Password' }).fill('Secret1234');
  await page.getByRole('button', { name: 'Sign in', exact: true }).click();
  await page.getByTestId('The Brutalist').click();
  await page.locator('video').click();
  await page.getByRole('button', { name: 'Logout' }).click();
});