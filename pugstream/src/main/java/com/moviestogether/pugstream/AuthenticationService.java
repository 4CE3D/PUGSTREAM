package com.moviestogether.pugstream;

import com.moviestogether.pugstream.Room.Room;
import com.moviestogether.pugstream.Room.RoomRepository;
import com.moviestogether.pugstream.Room.User;
import com.moviestogether.pugstream.Room.UserRepository;
import com.moviestogether.pugstream.auth.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.moviestogether.pugstream.Room.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse enterRoom(String name, long roomId, String password) {
        Optional<Room> roomOptional = roomRepository.findById(roomId);
        if (!roomOptional.isPresent()) {
            return new AuthenticationResponse();
        }

        Room room = roomOptional.get();

        // check if room is private and password is correct
        if ("private".equals(room.getType())) {
            BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
            if (!bcrypt.matches(password, room.getPassword())) {
                return new AuthenticationResponse();
            }
            String[] filenames = {
                    "_005e6d09-0151-4734-b124-bf7e44c30ba6.jpeg",
                    "_0169b3e1-1775-40e8-9e9c-20acbaa71281.jpeg",
                    "_041f9a2f-152e-4db2-af7e-b16132f7011a.jpeg",
                    "_06c2b5d0-54b3-463e-a3bb-500ed7e23ac8.jpeg",
                    "_0736dd15-9057-4b1d-affe-8d6f88415305.jpeg",
                    "_08eb27e4-c693-4fac-9758-c4c39409cab9.jpeg",
                    "_0fcbeacf-eb58-4718-a26f-1c5c086d6a5f.jpeg",
                    "_143b672e-915e-49da-905c-eb7f181f9df1.jpeg",
                    "_16318fa6-3939-48d1-9e17-69c0f7f51edf.jpeg",
                    "_1d41f7d7-b094-40b5-9be2-4f463be15be0.jpeg",
                    "_1f6c04d0-212f-46dd-9e4a-99aba839d6a9.jpeg",
                    "_206e4173-9ac8-484f-9edc-f567538a06c6.jpeg",
                    "_2281a70c-ab59-45a6-b075-e59694bc3d34.jpeg",
                    "_230b1cd4-7924-4b55-ac58-bf7f15031fcd.jpeg",
                    "_23db8fa2-5980-488a-8385-ee856a97f98a.jpeg",
                    "_240c414f-5065-4181-ae4b-1ed18b01d452.jpeg",
                    "_289fe07e-9ddd-45d8-a445-ffdbaa7dcf1f.jpeg",
                    "_2a0f2fe6-9ee7-4d7c-b01b-1b315b651517.jpeg",
                    "_2a7b687f-6b8e-41e4-954d-337272cb4919.jpeg",
                    "_2c266c98-574a-4942-8ddf-7b37d3672c09.jpeg",
                    "_30be109b-9d4a-4fb5-a714-97bc02dab541.jpeg",
                    "_31889b33-9dd1-4c89-ad68-1f3f8187dcb4.jpeg",
                    "_33f8e834-b2bf-4849-ba51-d0ba8b66f794.jpeg",
                    "_34bf261d-f26f-4e64-932f-5f28e5af2d34.jpeg",
                    "_36983841-dd33-411d-986f-e538f74e20aa.jpeg",
                    "_370d2c9d-e10a-4bef-b202-e0f2409e3abf.jpeg",
                    "_3b27aa5d-4a50-4f8c-af77-5b2e6412a8cb.jpeg",
                    "_3b4d6f42-1a27-4ce8-a1dc-4ee376791f87.jpeg",
                    "_3ba87fbf-e5ba-4e27-b832-a46ad9c5316a.jpeg",
                    "_3c5e4172-24c5-44a4-b728-729723c982c5.jpeg",
                    "_3cfee214-e3c7-4d7d-b30c-b7e87c4afc63.jpeg",
                    "_3d0aff1e-4fbf-421a-9c95-674a1062d9dd.jpeg",
                    "_3d424e0e-3c42-452b-b14e-19b525dc3fb0.jpeg",
                    "_42f442ab-d44b-455d-9cac-f452734dd6cc.jpeg",
                    "_43208218-d7c1-4326-835d-9de01febebb9.jpeg",
                    "_4758879e-37be-4c2f-af2f-223ef7f483b1.jpeg",
                    "_47888614-302c-4590-9af4-e519ab1c3c1c.jpeg",
                    "_480dc26a-0306-4ebe-9a9f-798a2103d194.jpeg",
                    "_4c1b1754-da40-42f2-8463-6a877365eb58.jpeg",
                    "_51042c79-d3d5-4006-aef9-5d98fbbd02f8.jpeg",
                    "_53c60ac9-7370-41d5-81e5-59aa9531b95d.jpeg",
                    "_553fc605-960a-4fe4-afb4-dd6bd53c6fc5.jpeg",
                    "_555823c6-c02a-4201-8b39-ae57a48b46dc.jpeg",
                    "_56ef64ce-e374-4231-a4fe-711af0a3a73d.jpeg",
                    "_593814c7-9372-4706-9b62-5160e59621f0.jpeg",
                    "_593d7e20-2be6-4daf-b719-8d91cc31961c.jpeg",
                    "_5b72a2fd-12e8-4314-9de9-159db26e9d74.jpeg",
                    "_5d8128f2-d9a2-4f35-aae5-0e931aaa75c9.jpeg",
                    "_614a0314-6e47-4dc5-9d01-8413b4fb4d8d.jpeg",
                    "_61d3de02-3c50-4e60-8ded-7b16775c0bad.jpeg",
                    "_635ea42f-b644-4fbc-911f-b7c9eeb18ba4.jpeg",
                    "_63dd71cb-efb4-4e37-83f3-38d70798f5a6.jpeg",
                    "_64ef789f-ac7f-447c-b911-d4f02e7f4f83.jpeg",
                    "_684b59db-829f-4a9a-9bce-8d2dd3b8ef03.jpeg",
                    "_686219cc-ff12-4da4-9f34-902f0d7bb588.jpeg",
                    "_68da5005-81c6-4edc-8349-8d020dbc196e.jpeg",
                    "_6b31e120-8d45-49ed-825d-60130732daaa.jpeg",
                    "_71d6e37f-cbe1-45f5-8692-4e840ecdc901.jpeg",
                    "_7512611e-9c2c-40d2-9277-7de4c8e17423.jpeg",
                    "_76b15a75-6d3c-4bb2-b2ac-2bf9557f6e21.jpeg",
                    "_77e59b3c-768c-45a6-b162-19ff39a6452e.jpeg",
                    "_7d08a039-ff91-4b91-974b-0877bcd262ff.jpeg",
                    "_80a76a12-0270-4fc3-a4a3-0386c8d05bf3.jpeg",
                    "_82fabd40-1800-4ec0-b5f2-c383bfc9a698.jpeg",
                    "_84cab5f4-1979-492b-a9b4-d4a5e3b3a21f.jpeg",
                    "_8598a40b-bc7f-4ce4-9a0f-2cd820e17215.jpeg",
                    "_86315bb0-c82c-4416-b2da-47537f37f850.jpeg",
                    "_874cc5fa-c1bf-4be7-b27e-729fa9c7551d.jpeg",
                    "_8c256dc1-3786-4d64-9744-b478f961e895.jpeg",
                    "_8e110aed-ed2e-4059-9af3-4832890efd63.jpeg",
                    "_91ee4888-6959-4de6-85a9-e379acfdf283.jpeg",
                    "_9521d0a6-d8ec-42f0-b140-613ab2553f35.jpeg",
                    "_98e59a71-4d11-4cdf-9536-7ee23c2c5092.jpeg",
                    "_9aa9caaa-bd57-42ef-839b-0eedc7b07b8f.jpeg",
                    "_9bcc6974-08fe-4c6a-879a-a5bb13ca15d9.jpeg",
                    "_9d563383-01e4-46db-a080-11f3b6c48b8b.jpeg",
                    "_adf72a62-6961-4fa9-99d2-d439188f4009.jpeg",
                    "_b1384e01-1e08-4528-ac3a-973ce2870a1d.jpeg",
                    "_b13fbe54-c1d9-4f5a-870e-5f596a836e6e.jpeg",
                    "_b51ccd3f-fa01-4867-8116-a63cd8e7a3e6.jpeg",
                    "_b74df168-d3da-4416-aebd-c08cca842835.jpeg",
                    "_b75b05aa-1664-4123-88af-b2773d6bd46d.jpeg",
                    "_bd679bc9-4438-46af-b631-92318fa7ccce.jpeg",
                    "_bde88a5f-da19-436e-9dde-7342f2195c43.jpeg",
                    "_bfb0d48b-fb7e-4d38-a5cc-92b98316a9a2.jpeg",
                    "_c24cc25e-25ae-4b45-be9f-eca2eab5317c.jpeg",
                    "_c282fa31-01e4-40aa-8a1f-f7f23ba184d3.jpeg",
                    "_c44ca315-4ce6-4386-82ce-e93168b4d512.jpeg",
                    "_c5b3dbb4-ddd3-4f9b-9255-cbb301b807aa.jpeg",
                    "_c6e544a2-fc70-4769-a41b-7d41ff9f81cc.jpeg",
                    "_ceb6e60a-09a7-498b-8791-dd4fd968f912.jpeg",
                    "_cec7f41b-94fc-46a3-9495-218c7a52cc75.jpeg",
                    "_cff883a1-38d7-417a-bb0f-acee2b0f271e.jpeg",
                    "_d0a2f5cd-c414-4b1e-afc2-da60485be50c.jpeg",
                    "_d4c51eac-e770-4ed9-8b9e-0d9cbb4d5679.jpeg",
                    "_dbcd8089-457f-4cdc-aa07-acbedcc3db05.jpeg",
                    "_dd421b14-d8fb-4408-a6e6-91a560ffabef.jpeg",
                    "_e288e186-abe9-42ef-af19-1be44e057e49.jpeg",
                    "_e6210aaa-2569-48c2-a0a0-f91e97a44171.jpeg",
                    "_e6602f25-d9c8-4b68-a45b-4419b114d66d.jpeg",
                    "_e9437b75-c001-4d38-b0f3-e41006ef861c.jpeg",
                    "_e9e81196-45b8-4c04-a425-0c2136b0e79a.jpeg",
                    "_ec8d34ba-a4d8-479b-b170-13fa502de1da.jpeg",
                    "_ed7b3eaa-bc24-4b36-b95f-54389c82b367.jpeg",
                    "_f32ae166-688a-4de8-aefa-0f29ef02d319.jpeg",
                    "_faa36bf2-1630-4089-90be-4fe558053dc0.jpeg",
                    "_fbf6b078-32c4-41a4-96c0-9395c0725477.jpeg"
            };

            Random random = new Random();
            int randomIndex = random.nextInt(filenames.length);
            String randomFilename = filenames[randomIndex];

            // creating user session and generating token
            User user = new User();
            user.setName(name);
            user.setAvatar("Pawatars/"+randomFilename);
            user.setRole(Role.USER);
            user.setRoom(room);
            userRepository.save(user);

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .room(room)
                    .build();
        }

        // room is public (no password check needed)
        if ("public".equals(room.getType())) {
            User user = new User();
            user.setName(name);
            user.setRole(Role.USER);
            user.setRoom(room);
            userRepository.save(user);

            String jwtToken = jwtService.generateToken(user);

            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .room(room)
                    .build();
        }

        return new AuthenticationResponse();  // if room type isn't 'private' or 'public'
    }

    public AuthenticationResponse createRoom(Room room) {
        String[] filenames = {
                "_005e6d09-0151-4734-b124-bf7e44c30ba6.jpeg",
                "_0169b3e1-1775-40e8-9e9c-20acbaa71281.jpeg",
                "_041f9a2f-152e-4db2-af7e-b16132f7011a.jpeg",
                "_06c2b5d0-54b3-463e-a3bb-500ed7e23ac8.jpeg",
                "_0736dd15-9057-4b1d-affe-8d6f88415305.jpeg",
                "_08eb27e4-c693-4fac-9758-c4c39409cab9.jpeg",
                "_0fcbeacf-eb58-4718-a26f-1c5c086d6a5f.jpeg",
                "_143b672e-915e-49da-905c-eb7f181f9df1.jpeg",
                "_16318fa6-3939-48d1-9e17-69c0f7f51edf.jpeg",
                "_1d41f7d7-b094-40b5-9be2-4f463be15be0.jpeg",
                "_1f6c04d0-212f-46dd-9e4a-99aba839d6a9.jpeg",
                "_206e4173-9ac8-484f-9edc-f567538a06c6.jpeg",
                "_2281a70c-ab59-45a6-b075-e59694bc3d34.jpeg",
                "_230b1cd4-7924-4b55-ac58-bf7f15031fcd.jpeg",
                "_23db8fa2-5980-488a-8385-ee856a97f98a.jpeg",
                "_240c414f-5065-4181-ae4b-1ed18b01d452.jpeg",
                "_289fe07e-9ddd-45d8-a445-ffdbaa7dcf1f.jpeg",
                "_2a0f2fe6-9ee7-4d7c-b01b-1b315b651517.jpeg",
                "_2a7b687f-6b8e-41e4-954d-337272cb4919.jpeg",
                "_2c266c98-574a-4942-8ddf-7b37d3672c09.jpeg",
                "_30be109b-9d4a-4fb5-a714-97bc02dab541.jpeg",
                "_31889b33-9dd1-4c89-ad68-1f3f8187dcb4.jpeg",
                "_33f8e834-b2bf-4849-ba51-d0ba8b66f794.jpeg",
                "_34bf261d-f26f-4e64-932f-5f28e5af2d34.jpeg",
                "_36983841-dd33-411d-986f-e538f74e20aa.jpeg",
                "_370d2c9d-e10a-4bef-b202-e0f2409e3abf.jpeg",
                "_3b27aa5d-4a50-4f8c-af77-5b2e6412a8cb.jpeg",
                "_3b4d6f42-1a27-4ce8-a1dc-4ee376791f87.jpeg",
                "_3ba87fbf-e5ba-4e27-b832-a46ad9c5316a.jpeg",
                "_3c5e4172-24c5-44a4-b728-729723c982c5.jpeg",
                "_3cfee214-e3c7-4d7d-b30c-b7e87c4afc63.jpeg",
                "_3d0aff1e-4fbf-421a-9c95-674a1062d9dd.jpeg",
                "_3d424e0e-3c42-452b-b14e-19b525dc3fb0.jpeg",
                "_42f442ab-d44b-455d-9cac-f452734dd6cc.jpeg",
                "_43208218-d7c1-4326-835d-9de01febebb9.jpeg",
                "_4758879e-37be-4c2f-af2f-223ef7f483b1.jpeg",
                "_47888614-302c-4590-9af4-e519ab1c3c1c.jpeg",
                "_480dc26a-0306-4ebe-9a9f-798a2103d194.jpeg",
                "_4c1b1754-da40-42f2-8463-6a877365eb58.jpeg",
                "_51042c79-d3d5-4006-aef9-5d98fbbd02f8.jpeg",
                "_53c60ac9-7370-41d5-81e5-59aa9531b95d.jpeg",
                "_553fc605-960a-4fe4-afb4-dd6bd53c6fc5.jpeg",
                "_555823c6-c02a-4201-8b39-ae57a48b46dc.jpeg",
                "_56ef64ce-e374-4231-a4fe-711af0a3a73d.jpeg",
                "_593814c7-9372-4706-9b62-5160e59621f0.jpeg",
                "_593d7e20-2be6-4daf-b719-8d91cc31961c.jpeg",
                "_5b72a2fd-12e8-4314-9de9-159db26e9d74.jpeg",
                "_5d8128f2-d9a2-4f35-aae5-0e931aaa75c9.jpeg",
                "_614a0314-6e47-4dc5-9d01-8413b4fb4d8d.jpeg",
                "_61d3de02-3c50-4e60-8ded-7b16775c0bad.jpeg",
                "_635ea42f-b644-4fbc-911f-b7c9eeb18ba4.jpeg",
                "_63dd71cb-efb4-4e37-83f3-38d70798f5a6.jpeg",
                "_64ef789f-ac7f-447c-b911-d4f02e7f4f83.jpeg",
                "_684b59db-829f-4a9a-9bce-8d2dd3b8ef03.jpeg",
                "_686219cc-ff12-4da4-9f34-902f0d7bb588.jpeg",
                "_68da5005-81c6-4edc-8349-8d020dbc196e.jpeg",
                "_6b31e120-8d45-49ed-825d-60130732daaa.jpeg",
                "_71d6e37f-cbe1-45f5-8692-4e840ecdc901.jpeg",
                "_7512611e-9c2c-40d2-9277-7de4c8e17423.jpeg",
                "_76b15a75-6d3c-4bb2-b2ac-2bf9557f6e21.jpeg",
                "_77e59b3c-768c-45a6-b162-19ff39a6452e.jpeg",
                "_7d08a039-ff91-4b91-974b-0877bcd262ff.jpeg",
                "_80a76a12-0270-4fc3-a4a3-0386c8d05bf3.jpeg",
                "_82fabd40-1800-4ec0-b5f2-c383bfc9a698.jpeg",
                "_84cab5f4-1979-492b-a9b4-d4a5e3b3a21f.jpeg",
                "_8598a40b-bc7f-4ce4-9a0f-2cd820e17215.jpeg",
                "_86315bb0-c82c-4416-b2da-47537f37f850.jpeg",
                "_874cc5fa-c1bf-4be7-b27e-729fa9c7551d.jpeg",
                "_8c256dc1-3786-4d64-9744-b478f961e895.jpeg",
                "_8e110aed-ed2e-4059-9af3-4832890efd63.jpeg",
                "_91ee4888-6959-4de6-85a9-e379acfdf283.jpeg",
                "_9521d0a6-d8ec-42f0-b140-613ab2553f35.jpeg",
                "_98e59a71-4d11-4cdf-9536-7ee23c2c5092.jpeg",
                "_9aa9caaa-bd57-42ef-839b-0eedc7b07b8f.jpeg",
                "_9bcc6974-08fe-4c6a-879a-a5bb13ca15d9.jpeg",
                "_9d563383-01e4-46db-a080-11f3b6c48b8b.jpeg",
                "_adf72a62-6961-4fa9-99d2-d439188f4009.jpeg",
                "_b1384e01-1e08-4528-ac3a-973ce2870a1d.jpeg",
                "_b13fbe54-c1d9-4f5a-870e-5f596a836e6e.jpeg",
                "_b51ccd3f-fa01-4867-8116-a63cd8e7a3e6.jpeg",
                "_b74df168-d3da-4416-aebd-c08cca842835.jpeg",
                "_b75b05aa-1664-4123-88af-b2773d6bd46d.jpeg",
                "_bd679bc9-4438-46af-b631-92318fa7ccce.jpeg",
                "_bde88a5f-da19-436e-9dde-7342f2195c43.jpeg",
                "_bfb0d48b-fb7e-4d38-a5cc-92b98316a9a2.jpeg",
                "_c24cc25e-25ae-4b45-be9f-eca2eab5317c.jpeg",
                "_c282fa31-01e4-40aa-8a1f-f7f23ba184d3.jpeg",
                "_c44ca315-4ce6-4386-82ce-e93168b4d512.jpeg",
                "_c5b3dbb4-ddd3-4f9b-9255-cbb301b807aa.jpeg",
                "_c6e544a2-fc70-4769-a41b-7d41ff9f81cc.jpeg",
                "_ceb6e60a-09a7-498b-8791-dd4fd968f912.jpeg",
                "_cec7f41b-94fc-46a3-9495-218c7a52cc75.jpeg",
                "_cff883a1-38d7-417a-bb0f-acee2b0f271e.jpeg",
                "_d0a2f5cd-c414-4b1e-afc2-da60485be50c.jpeg",
                "_d4c51eac-e770-4ed9-8b9e-0d9cbb4d5679.jpeg",
                "_dbcd8089-457f-4cdc-aa07-acbedcc3db05.jpeg",
                "_dd421b14-d8fb-4408-a6e6-91a560ffabef.jpeg",
                "_e288e186-abe9-42ef-af19-1be44e057e49.jpeg",
                "_e6210aaa-2569-48c2-a0a0-f91e97a44171.jpeg",
                "_e6602f25-d9c8-4b68-a45b-4419b114d66d.jpeg",
                "_e9437b75-c001-4d38-b0f3-e41006ef861c.jpeg",
                "_e9e81196-45b8-4c04-a425-0c2136b0e79a.jpeg",
                "_ec8d34ba-a4d8-479b-b170-13fa502de1da.jpeg",
                "_ed7b3eaa-bc24-4b36-b95f-54389c82b367.jpeg",
                "_f32ae166-688a-4de8-aefa-0f29ef02d319.jpeg",
                "_faa36bf2-1630-4089-90be-4fe558053dc0.jpeg",
                "_fbf6b078-32c4-41a4-96c0-9395c0725477.jpeg"
        };

        Random random = new Random();
        int randomIndex = random.nextInt(filenames.length);
        String randomFilename = filenames[randomIndex];

        User user = new User();
        user.setRoom(room);
        user.setAvatar("Pawatars/"+randomFilename);
        user.setName("PUG");
        user.setRole(Role.ADMIN);
        userRepository.save(user);
        var userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(String.valueOf(Role.USER))
                .build();

        var jwtToken = jwtService.generateToken(userDetails);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .room(room)
                .build();
    }
}
