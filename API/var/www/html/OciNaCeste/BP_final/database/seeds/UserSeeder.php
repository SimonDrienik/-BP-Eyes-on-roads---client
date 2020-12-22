<?php

use Illuminate\Database\Seeder;
use App\User;
use Illuminate\Support\Facades\Hash;

class UserSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $users = [
            [
                'name' => 'Nezaregistrovaný občan',
                'email' => '-',
                'password' => '-',
                'rola_id' => 2
            ],
            /*

            [
                'name' => 'System admin',
                'email' => 'system@admin',
                'password' => 'system_Admin_12345_ForCaseOfNeed',
                'rola_id' => 3
            ]
            */
        ];

        foreach ($users as $user) {
            User::create(array(

                'name' => $user['name'],
                'email' => $user['email'],
                'password' => Hash::make($user['password']),
                'rola_id' => $user['rola_id']
            ));
        }
    }
}
