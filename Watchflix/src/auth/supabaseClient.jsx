import { createClient } from '@supabase/supabase-js';
import config from '../config';

const supabaseUrl = config.supabaseUrl;
const supabaseAnonKey = config.supabaseAnonKey; 

const supabaseClient = createClient(supabaseUrl, supabaseAnonKey);

export default supabaseClient;